package com.fag.gunshopapi.controller;

import com.fag.gunshopapi.dto.WeaponDTO;
import com.fag.gunshopapi.service.WeaponService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/armas")
public class WeaponController {

    private final WeaponService service;

    public WeaponController(WeaponService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<WeaponDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeaponDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<WeaponDTO> create(@Valid @RequestBody WeaponDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeaponDTO> update(@PathVariable Integer id, @Valid @RequestBody WeaponDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
