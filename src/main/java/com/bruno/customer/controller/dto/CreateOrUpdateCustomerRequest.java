package com.bruno.customer.controller.dto;

public record CreateOrUpdateCustomerRequest(
        String name,
        String cpf,
        String email,
        String phone) {

}
