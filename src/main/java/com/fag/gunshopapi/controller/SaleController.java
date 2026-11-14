package com.fag.gunshopapi.controller;

import com.fag.gunshopapi.dto.SaleRequestDTO;
import com.fag.gunshopapi.dto.SaleResponseDTO;
import com.fag.gunshopapi.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class SaleController {

    private final SaleService service;

    public SaleController(SaleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<SaleResponseDTO> create(@Valid @RequestBody SaleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<SaleResponseDTO> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(service.cancelSale(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
