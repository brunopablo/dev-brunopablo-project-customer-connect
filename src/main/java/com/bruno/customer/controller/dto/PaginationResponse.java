/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bruno.customer.controller.dto;


public record PaginationResponse(
    Integer pageNumber,
    Integer pageSize,
    Long pageTotalElements,
    Integer pageTotal
) {

}
