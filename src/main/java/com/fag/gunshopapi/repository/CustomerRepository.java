package com.fag.gunshopapi.repository;

import com.fag.gunshopapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByCpf(String cpf);
}
