package com.bruno.customer.controller.dto;


public record PaginationResponse(
    Integer pageNumber,
    Integer pageSize,
    Long pageTotalElements,
    Integer pageTotal
) {

}
