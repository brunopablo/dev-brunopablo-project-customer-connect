package com.bruno.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bruno.customer.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Page<CustomerEntity> findByEmail(String email, PageRequest pageRequest);

    Page<CustomerEntity> findByCpf(String cpf, PageRequest pageRequest);

    Page<CustomerEntity> findByCpfAndEmail(String email, String cpf,
            PageRequest pageRequest);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}
