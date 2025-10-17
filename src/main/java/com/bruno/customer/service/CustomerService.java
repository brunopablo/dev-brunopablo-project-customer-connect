package com.bruno.customer.service;

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

        if (customerRepository.existsByCpf(createOrUpdateClientRequest.cpf())) {
            System.out.println("CPF JA EXISTE!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF is registered!");
        }

        if (customerRepository.existsByEmail(createOrUpdateClientRequest.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL is registered!");
        }

        var customerEntity = new CustomerEntity(
                null,
                createOrUpdateClientRequest.name(),
                createOrUpdateClientRequest.cpf(),
                createOrUpdateClientRequest.email(),
                createOrUpdateClientRequest.phone(),
                null,
                null);

        var customerCreated = customerRepository.save(customerEntity);

        return customerCreated.getId();
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
                && !createOrUpdateCustomerRequest.name().trim().equalsIgnoreCase(customerEntity.getName())) {

            customerEntity.setName(createOrUpdateCustomerRequest.name());
        }

        if ((Objects.nonNull(createOrUpdateCustomerRequest.email())
                && !createOrUpdateCustomerRequest.email().trim().isEmpty())
                && !createOrUpdateCustomerRequest.email().trim().equalsIgnoreCase(customerEntity.getEmail())) {

            customerEntity.setEmail(createOrUpdateCustomerRequest.email());
        }

        if ((Objects.nonNull(createOrUpdateCustomerRequest.phone())
                && !createOrUpdateCustomerRequest.phone().trim().isEmpty())
                && !createOrUpdateCustomerRequest.phone().trim().equalsIgnoreCase(customerEntity.getPhone())) {

            customerEntity.setPhone(createOrUpdateCustomerRequest.phone());
        }

        if (((Objects.nonNull(createOrUpdateCustomerRequest.cpf())
                && !createOrUpdateCustomerRequest.cpf().trim().isEmpty())
                && !createOrUpdateCustomerRequest.cpf().equals(customerEntity.getCpf()))) {

            customerEntity.setCpf(createOrUpdateCustomerRequest.cpf());
        }

        customerRepository.save(customerEntity);
    }

    public ApiResponse<CustomerEntity> findAllCustomers(
            Integer page,
            Integer pageSize,
            String orderBy,
            String customerEmail,
            String customerCpf) {

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

    private Page<CustomerEntity> getPages(String customerEmail, String customerCpf, PageRequest pageRequest) {

        System.out.println(customerEmail);
        System.out.println(customerCpf);

        if ((Objects.nonNull(customerEmail) && !customerEmail.trim().isEmpty())
                && (Objects.nonNull(customerCpf) && !customerCpf.trim().isBlank())) {
            System.out.println("Entrou no primeiro if");
            return customerRepository.findByCpfAndEmail(customerCpf, customerEmail, pageRequest);
        }

        if (Objects.nonNull(customerEmail) && !customerEmail.trim().isEmpty()) {
            System.out.println("Entrou no segundo if");
            return customerRepository.findByEmail(customerEmail, pageRequest);
        }
        
        if (Objects.nonNull(customerCpf) && !customerCpf.trim().isBlank()) {
            System.out.println("Entrou no terceiro if");
            return customerRepository.findByCpf(customerCpf, pageRequest);
        }
        
        System.out.println("Entrou em nenhum dos ifs");
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
