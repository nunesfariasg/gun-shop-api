package com.fag.gunshopapi.controller;

import com.fag.gunshopapi.dto.AmmunitionDTO;
import com.fag.gunshopapi.service.AmmunitionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/municoes")
public class AmmunitionController {

    private final AmmunitionService service;

    public AmmunitionController(AmmunitionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AmmunitionDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmmunitionDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AmmunitionDTO> create(@Valid @RequestBody AmmunitionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmmunitionDTO> update(@PathVariable Integer id, @Valid @RequestBody AmmunitionDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

