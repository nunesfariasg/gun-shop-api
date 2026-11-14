package com.fag.gunshopapi.service;

import com.fag.gunshopapi.dto.CustomerDTO;
import com.fag.gunshopapi.entity.Customer;
import com.fag.gunshopapi.exception.BusinessRuleException;
import com.fag.gunshopapi.exception.ResourceNotFoundException;
import com.fag.gunshopapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<CustomerDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public CustomerDTO findById(Integer id) {
        return toDTO(getEntity(id));
    }

    public CustomerDTO create(CustomerDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new BusinessRuleException("Já existe um cliente cadastrado com este CPF.");
        }
        Customer entity = new Customer();
        updateEntity(entity, dto);
        return toDTO(repository.save(entity));
    }

    public CustomerDTO update(Integer id, CustomerDTO dto) {
        Customer entity = getEntity(id);
        updateEntity(entity, dto);
        return toDTO(repository.save(entity));
    }

    public void delete(Integer id) {
        Customer entity = getEntity(id);
        repository.delete(entity);
    }

    private Customer getEntity(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }

    private void updateEntity(Customer entity, CustomerDTO dto) {
        entity.setName(dto.name());
        entity.setCpf(dto.cpf());
        entity.setBirthDate(dto.birthDate());
        entity.setCrNumber(dto.crNumber());
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getId(), c.getName(), c.getCpf(), c.getBirthDate(), c.getCrNumber());
    }
}
