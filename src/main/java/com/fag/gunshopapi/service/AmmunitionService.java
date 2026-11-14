package com.fag.gunshopapi.service;

import com.fag.gunshopapi.dto.AmmunitionDTO;
import com.fag.gunshopapi.entity.Ammunition;
import com.fag.gunshopapi.exception.ResourceNotFoundException;
import com.fag.gunshopapi.repository.AmmunitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmmunitionService {

    private final AmmunitionRepository repository;

    public AmmunitionService(AmmunitionRepository repository) {
        this.repository = repository;
    }

    public List<AmmunitionDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public AmmunitionDTO findById(Integer id) {
        return toDTO(getEntity(id));
    }

    public AmmunitionDTO create(AmmunitionDTO dto) {
        Ammunition entity = new Ammunition();
        updateEntity(entity, dto);
        return toDTO(repository.save(entity));
    }

    public AmmunitionDTO update(Integer id, AmmunitionDTO dto) {
        Ammunition entity = getEntity(id);
        updateEntity(entity, dto);
        return toDTO(repository.save(entity));
    }

    public void delete(Integer id) {
        Ammunition entity = getEntity(id);
        repository.delete(entity);
    }

    private Ammunition getEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Munição não encontrada com ID: " + id));
    }

    private void updateEntity(Ammunition entity, AmmunitionDTO dto) {
        entity.setBrand(dto.brand());
        entity.setCaliber(dto.caliber());
        entity.setQuantityPerBox(dto.quantityPerBox());
        entity.setPrice(dto.price());
        entity.setStock(dto.stock());
    }

    private AmmunitionDTO toDTO(Ammunition a) {
        return new AmmunitionDTO(a.getId(), a.getBrand(), a.getCaliber(), a.getQuantityPerBox(), a.getPrice(), a.getStock());
    }
}
