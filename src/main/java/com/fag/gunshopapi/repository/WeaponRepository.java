package com.fag.gunshopapi.repository;

import com.fag.gunshopapi.entity.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeaponRepository extends JpaRepository<Weapon, Integer> {
}
