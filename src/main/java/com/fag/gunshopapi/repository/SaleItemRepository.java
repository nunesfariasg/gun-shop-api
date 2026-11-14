package com.fag.gunshopapi.repository;

import com.fag.gunshopapi.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {

    @Query("SELECT COUNT(si) > 0 FROM SaleItem si " +
            "JOIN si.sale s " +
            "JOIN si.weapon w " +
            "WHERE s.customer.id = :customerId " +
            "AND LOWER(w.caliber) = LOWER(:caliber) " +
            "AND s.status = 'APPROVED'")
    boolean hasWeaponOfCaliberForCustomer(@Param("customerId") Integer customerId,
                                          @Param("caliber") String caliber);
}