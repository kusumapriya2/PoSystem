package com.example.controller;

import com.example.dto.CustomerDto;
import com.example.dto.ProductDTO;
import com.example.response.ApiResponse;
import com.example.service.CustomerService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired CustomerService customerService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;
    @PostMapping("/createCustomer")
    public ResponseEntity<ApiResponse<CustomerDto>> create(@Valid @RequestBody CustomerDto dto){
        return customerService.createCustomer(dto);
    }
    @GetMapping("/getAllCustomers")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getByIdCustomer(@PathVariable Long id){
        return customerService.getByIdCustomer(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@RequestBody CustomerDto dto,@PathVariable Long id){
        return customerService.updateByIdCustomer(dto,id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }@GetMapping("/getAllProductsByName/{name}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProductsByName(
            @PathVariable String name) {

        return productService.getAllProductsBySearch(name);
    }



}
