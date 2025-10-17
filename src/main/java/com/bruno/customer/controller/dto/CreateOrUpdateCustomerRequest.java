package com.bruno.customer.controller.dto;

public record CreateOrUpdateCustomerRequest(
        String name,
        Long cpf,
        String email,
        String phone) {

}
