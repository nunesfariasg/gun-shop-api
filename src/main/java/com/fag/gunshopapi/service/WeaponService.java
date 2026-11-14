package com.fag.gunshopapi.service;

import com.fag.gunshopapi.dto.WeaponDTO;
import com.fag.gunshopapi.entity.Weapon;
import com.fag.gunshopapi.exception.ResourceNotFoundException;
import com.fag.gunshopapi.repository.WeaponRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeaponService {

    private final WeaponRepository repository;

    public WeaponService(WeaponRepository repository) {
        this.repository = repository;
    }

    public List<WeaponDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public WeaponDTO findById(Integer id) {
        return toDTO(getEntity(id));
    }

    public WeaponDTO create(WeaponDTO dto) {
        Weapon entity = new Weapon();
        updateEntity(entity, dto);
        return toDTO(repository.save(entity));
    }

    public WeaponDTO update(Integer id, WeaponDTO dto) {
        Weapon entity = getEntity(id);
        updateEntity(entity, dto);
        return toDTO(repository.save(entity));
    }

    public void delete(Integer id) {
        Weapon entity = getEntity(id);
        repository.delete(entity);
    }

    private Weapon getEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arma não encontrada com ID: " + id));
    }

    private void updateEntity(Weapon entity, WeaponDTO dto) {
        entity.setModel(dto.model());
        entity.setBrand(dto.brand());
        entity.setCaliber(dto.caliber());
        entity.setType(dto.type());
        entity.setPrice(dto.price());
        entity.setStock(dto.stock());
    }

    private WeaponDTO toDTO(Weapon w) {
        return new WeaponDTO(w.getId(), w.getModel(), w.getBrand(), w.getCaliber(), w.getType(), w.getPrice(), w.getStock());
    }
}