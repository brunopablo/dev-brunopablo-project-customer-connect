package com.bruno.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bruno.customer.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Page<CustomerEntity> findByCustomerEmail(String customerEmail, PageRequest pageRequest);

    Page<CustomerEntity> findByCustomerCpf(Long customerCpf, PageRequest pageRequest);

    Page<CustomerEntity> findByCustomerEmailAndCustomerCpf(String customerEmail, Long customerCpf,
            PageRequest pageRequest);

    boolean existsByCustomerCpf(Long customerCpf);

    boolean existsByCustomerEmail(String customerEmail);
}
