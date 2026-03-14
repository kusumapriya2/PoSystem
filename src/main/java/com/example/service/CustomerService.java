package com.example.service;
import com.example.dto.CustomerDto;
import com.example.entity.Customer;
import com.example.mapper.CustomerMapper;
import com.example.repository.CustomerRepo;
import com.example.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    CustomerMapper customerMapper;
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(CustomerDto dto) {
        try {
            Customer saved = customerRepo.save(customerMapper.toEntity(dto));
            return ResponseEntity.ok(ApiResponse.success("customer Created", customerMapper.toDTO(saved)));
        } catch (Exception e) {
            log.error("Create customer failed", e);
            return ResponseEntity.internalServerError().body(ApiResponse.fail("Failed to create customer"));
        }
    }
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers() {
            try{
                List<CustomerDto> list=customerRepo.findAll().stream()
                        .map(customerMapper::toDTO)
                        .toList();
                return ResponseEntity.ok(ApiResponse.success("customers fetched",list));
            } catch (Exception e) {
                log.error("Get all customers failed",e);
                return ResponseEntity.internalServerError().body(ApiResponse.fail("Failed to fetch customers"));
            }
        }
        public ResponseEntity<ApiResponse<CustomerDto>> getByIdCustomer(Long id) {
        try {
            Customer customer = customerRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("customer not found"));
            return ResponseEntity.ok(ApiResponse.success("customer fetched", customerMapper.toDTO(customer)));
        } catch (Exception e) {
            log.error("get customer failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<CustomerDto>> updateByIdCustomer(CustomerDto dto, Long id) {
        try {
            Customer existing = customerRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("customer not found"));
            customerMapper.update(dto, existing);
            Customer saved = customerRepo.save(existing);
            return ResponseEntity.ok(ApiResponse.success("customer updated", customerMapper.toDTO(saved)));
        } catch (Exception e) {
            log.error("Update poitem failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<CustomerDto>> deleteCustomer(Long id) {
        try {
            if (!customerRepo.existsById(id)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.fail("customer not found"));
            }
            customerRepo.deleteById(id);
            return ResponseEntity.ok(
                    ApiResponse.success("customer deleted succesfully"));
        } catch (Exception e) {
            log.error("Delete customer failed", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Failed to delete customer"));
        }
    }

}