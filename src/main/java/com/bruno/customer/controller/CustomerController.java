package com.bruno.customer.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.customer.controller.dto.ApiResponse;
import com.bruno.customer.controller.dto.CreateOrUpdateCustomerRequest;
import com.bruno.customer.entity.CustomerEntity;
import com.bruno.customer.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Long> createCustomer(
            @RequestBody CreateOrUpdateCustomerRequest createOrUpdateCustomerRequest) {

        var customerId = customerService.createCustomer(createOrUpdateCustomerRequest);

        return ResponseEntity.created(URI.create("/customers/" + customerId)).build();
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {

        customerService.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Void> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CreateOrUpdateCustomerRequest createOrUpdateCustomerRequest) {

        customerService.updateUser(customerId, createOrUpdateCustomerRequest);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerEntity>> listAllCustomers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "orderBy", defaultValue = "desc") String orderBy,
            @RequestParam(name = "customerEmail", required = false) String customerEmail,
            @RequestParam(name = "customerCpf", required = false) String customerCpf) {

        var apiResponse = customerService.findAllCustomers(
                page,
                pageSize,
                orderBy,
                customerEmail,
                customerCpf);

        
        return ResponseEntity.ok(apiResponse);
    }
}
