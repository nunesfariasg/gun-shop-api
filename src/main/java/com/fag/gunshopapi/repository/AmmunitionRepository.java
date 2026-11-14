package com.fag.gunshopapi.repository;

import com.fag.gunshopapi.entity.Ammunition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmmunitionRepository extends JpaRepository<Ammunition, Integer> {
}
