package com.fag.gunshopapi.service;

import com.fag.gunshopapi.dto.*;
import com.fag.gunshopapi.entity.*;
import com.fag.gunshopapi.exception.BusinessRuleException;
import com.fag.gunshopapi.exception.ResourceNotFoundException;
import com.fag.gunshopapi.exception.UnprocessableEntityException;
import com.fag.gunshopapi.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final WeaponRepository weaponRepository;
    private final AmmunitionRepository ammunitionRepository;
    private final SaleItemRepository saleItemRepository;

    public SaleService(SaleRepository saleRepository, CustomerRepository customerRepository,
                       WeaponRepository weaponRepository, AmmunitionRepository ammunitionRepository,
                       SaleItemRepository saleItemRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.weaponRepository = weaponRepository;
        this.ammunitionRepository = ammunitionRepository;
        this.saleItemRepository = saleItemRepository;
    }

    public List<SaleResponseDTO> findAll() {
        return saleRepository.findAll().stream().map(this::toResponseDTO).toList();
    }

    public SaleResponseDTO findById(Integer id) {
        return toResponseDTO(getEntity(id));
    }

    @Transactional
    public SaleResponseDTO create(SaleRequestDTO dto) {
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + dto.customerId()));

        // Regra 3: Idade >= 25 anos e CR válido
        int age = Period.between(customer.getBirthDate(), LocalDate.now()).getYears();
        if (age < 25) {
            throw new BusinessRuleException("Venda negada: cliente deve ter no mínimo 25 anos.");
        }
        if (customer.getCrNumber() == null || customer.getCrNumber().isBlank()) {
            throw new BusinessRuleException("Venda negada: cliente não possui CR válido.");
        }

        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setSaleDate(LocalDateTime.now());
        sale.setStatus("APPROVED");

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleItem> items = new ArrayList<>();

        // Coleta calibres de armas compradas nesta mesma venda
        List<String> weaponCalibersInCart = new ArrayList<>();
        for (SaleItemRequestDTO itemDto : dto.items()) {
            if (itemDto.weaponId() != null) {
                Weapon w = weaponRepository.findById(itemDto.weaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Arma não encontrada com ID: " + itemDto.weaponId()));
                weaponCalibersInCart.add(w.getCaliber().toLowerCase());
            }
        }

        for (SaleItemRequestDTO itemDto : dto.items()) {
            boolean hasWeapon = itemDto.weaponId() != null;
            boolean hasAmmo = itemDto.ammunitionId() != null;

            if (hasWeapon == hasAmmo) {
                throw new BusinessRuleException("Cada item deve conter exclusivamente uma arma OU uma munição.");
            }

            SaleItem item = new SaleItem();
            item.setSale(sale);
            item.setQuantity(itemDto.quantity());

            if (hasWeapon) {
                Weapon weapon = weaponRepository.findById(itemDto.weaponId())
                        .orElseThrow(() -> new ResourceNotFoundException("Arma não encontrada com ID: " + itemDto.weaponId()));

                // Regra 2: Abate de estoque
                if (weapon.getStock() < itemDto.quantity()) {
                    throw new BusinessRuleException("Estoque insuficiente para a arma: " + weapon.getModel());
                }
                weapon.setStock(weapon.getStock() - itemDto.quantity());
                weaponRepository.save(weapon);

                item.setWeapon(weapon);
                item.setUnitPrice(weapon.getPrice());
                totalAmount = totalAmount.add(weapon.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
            } else {
                Ammunition ammo = ammunitionRepository.findById(itemDto.ammunitionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Munição não encontrada com ID: " + itemDto.ammunitionId()));

                // Regra 1: Validação de calibre (histórico ou no mesmo carrinho)
                boolean ownsCaliber = saleItemRepository.hasWeaponOfCaliberForCustomer(customer.getId(), ammo.getCaliber());
                boolean buyingCaliberNow = weaponCalibersInCart.contains(ammo.getCaliber().toLowerCase());

                if (!ownsCaliber && !buyingCaliberNow) {
                    throw new UnprocessableEntityException("Cliente não possui arma registrada no calibre: " + ammo.getCaliber());
                }

                // Regra 2: Abate de estoque
                if (ammo.getStock() < itemDto.quantity()) {
                    throw new BusinessRuleException("Estoque insuficiente para a munição calibre: " + ammo.getCaliber());
                }
                ammo.setStock(ammo.getStock() - itemDto.quantity());
                ammunitionRepository.save(ammo);

                item.setAmmunition(ammo);
                item.setUnitPrice(ammo.getPrice());
                totalAmount = totalAmount.add(ammo.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
            }

            items.add(item);
        }

        sale.setTotalAmount(totalAmount);
        sale.setItems(items);

        return toResponseDTO(saleRepository.save(sale));
    }

    @Transactional
    public SaleResponseDTO cancelSale(Integer id) {
        Sale sale = getEntity(id);
        if ("CANCELED".equals(sale.getStatus())) {
            throw new BusinessRuleException("Esta venda já está cancelada.");
        }

        // Devolve os itens ao estoque
        for (SaleItem item : sale.getItems()) {
            if (item.getWeapon() != null) {
                Weapon w = item.getWeapon();
                w.setStock(w.getStock() + item.getQuantity());
                weaponRepository.save(w);
            } else if (item.getAmmunition() != null) {
                Ammunition a = item.getAmmunition();
                a.setStock(a.getStock() + item.getQuantity());
                ammunitionRepository.save(a);
            }
        }

        sale.setStatus("CANCELED");
        return toResponseDTO(saleRepository.save(sale));
    }

    @Transactional
    public void delete(Integer id) {
        Sale sale = getEntity(id);
        if (!"CANCELED".equals(sale.getStatus())) {
            cancelSale(id);
        }
        saleRepository.delete(sale);
    }

    private Sale getEntity(Integer id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com ID: " + id));
    }

    private SaleResponseDTO toResponseDTO(Sale sale) {
        List<SaleItemResponseDTO> itemDTOs = sale.getItems().stream().map(item -> {
            if (item.getWeapon() != null) {
                return new SaleItemResponseDTO(
                        item.getId(),
                        "WEAPON",
                        item.getWeapon().getBrand() + " " + item.getWeapon().getModel(),
                        item.getWeapon().getCaliber(),
                        item.getQuantity(),
                        item.getUnitPrice()
                );
            } else {
                return new SaleItemResponseDTO(
                        item.getId(),
                        "AMMUNITION",
                        item.getAmmunition().getBrand() + " (" + item.getAmmunition().getQuantityPerBox() + " un.)",
                        item.getAmmunition().getCaliber(),
                        item.getQuantity(),
                        item.getUnitPrice()
                );
            }
        }).toList();

        return new SaleResponseDTO(
                sale.getId(),
                sale.getCustomer().getId(),
                sale.getCustomer().getName(),
                sale.getSaleDate(),
                sale.getTotalAmount(),
                sale.getStatus(),
                itemDTOs
        );
    }
}
