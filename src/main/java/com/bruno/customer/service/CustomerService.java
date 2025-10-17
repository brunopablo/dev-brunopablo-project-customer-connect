package com.bruno.customer.service;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bruno.customer.controller.dto.ApiResponse;
import com.bruno.customer.controller.dto.CreateOrUpdateCustomerRequest;
import com.bruno.customer.controller.dto.PaginationResponse;
import com.bruno.customer.entity.CustomerEntity;
import com.bruno.customer.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Long createCustomer(CreateOrUpdateCustomerRequest createOrUpdateClientRequest) {

        if (customerRepository.existsByCustomerCpf(createOrUpdateClientRequest.cpf())) {
            System.out.println("CPF JA EXISTE!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF is registered!");
        }

        if (customerRepository.existsByCustomerEmail(createOrUpdateClientRequest.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL is registered!");
        }

        var customerEntity = new CustomerEntity(
                null,
                createOrUpdateClientRequest.name(),
                createOrUpdateClientRequest.cpf(),
                createOrUpdateClientRequest.email(),
                createOrUpdateClientRequest.phone(),
                LocalDateTime.now(),
                null);

        var customerCreated = customerRepository.save(customerEntity);

        return customerCreated.getCustomerId();
    }

    public void deleteCustomer(Long customerId) {

        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found!");
        }
    }

    public void updateUser(
            Long customerId,
            CreateOrUpdateCustomerRequest createOrUpdateCustomerRequest) {

        var customerEntity = customerRepository.findById(customerId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Customer Not Found"));

        if ((Objects.nonNull(createOrUpdateCustomerRequest.name())
                && !createOrUpdateCustomerRequest.name().trim().isEmpty())
                && !createOrUpdateCustomerRequest.name().trim().equalsIgnoreCase(customerEntity.getCustomerName())) {

            customerEntity.setCustomerName(createOrUpdateCustomerRequest.name());
        }

        if ((Objects.nonNull(createOrUpdateCustomerRequest.email())
                && !createOrUpdateCustomerRequest.email().trim().isEmpty())
                && !createOrUpdateCustomerRequest.email().trim().equalsIgnoreCase(customerEntity.getCustomerEmail())) {

            customerEntity.setCustomerEmail(createOrUpdateCustomerRequest.email());
        }

        if ((Objects.nonNull(createOrUpdateCustomerRequest.phone())
                && !createOrUpdateCustomerRequest.phone().trim().isEmpty())
                && !createOrUpdateCustomerRequest.phone().trim().equalsIgnoreCase(customerEntity.getCustomerPhone())) {

            customerEntity.setCustomerPhone(createOrUpdateCustomerRequest.phone());
        }

        if ((Objects.nonNull(createOrUpdateCustomerRequest.cpf())
                && !createOrUpdateCustomerRequest.cpf().equals(customerEntity.getCustomerCpf()))) {

            customerEntity.setCustomerCpf(createOrUpdateCustomerRequest.cpf());
        }

        customerRepository.save(customerEntity);
    }

    public ApiResponse<CustomerEntity> findAllCustomers(
            Integer page,
            Integer pageSize,
            String orderBy,
            String customerEmail,
            Long customerCpf) {

        var pageRequest = getPageRequest(page, pageSize, orderBy);

        var pages = getPages(customerEmail, customerCpf, pageRequest);

        return new ApiResponse<>(
                pages.getContent(),
                new PaginationResponse(
                        pages.getNumber(),
                        pages.getSize(),
                        pages.getTotalElements(),
                        pages.getTotalPages()));

    }

    private Page<CustomerEntity> getPages(String customerEmail, Long customerCpf, PageRequest pageRequest) {
        if ((Objects.nonNull(customerEmail) && !customerEmail.trim().isEmpty())
                && Objects.nonNull(customerCpf)) {
            return customerRepository.findByCustomerEmailAndCustomerCpf(customerEmail, customerCpf, pageRequest);
        }

        if (Objects.nonNull(customerEmail) && !customerEmail.trim().isEmpty()) {
            return customerRepository.findByCustomerEmail(customerEmail, pageRequest);
        }

        if (Objects.nonNull(customerCpf)) {
            return customerRepository.findByCustomerCpf(customerCpf, pageRequest);
        }

        return customerRepository.findAll(pageRequest);
    }

    private PageRequest getPageRequest(Integer page, Integer pageSize, String orderBy) {
        Direction orderByDirection = Sort.Direction.DESC;

        if (orderBy.equalsIgnoreCase("asc")) {
            orderByDirection = Sort.Direction.ASC;
        }

        return PageRequest.of(
                page,
                pageSize,
                orderByDirection,
                "createdAt");
    }

}
