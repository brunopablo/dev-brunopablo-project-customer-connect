package com.bruno.customer.controller.dto;

import java.util.List;

public record ApiResponse<T>(
    List<T> content,
    PaginationResponse paginationResponse
) {

}
