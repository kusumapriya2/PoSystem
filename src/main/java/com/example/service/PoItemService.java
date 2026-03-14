package com.example.service;
import com.example.dto.PoItemDto;
import com.example.entity.Order;
import com.example.entity.PoItem;
import com.example.entity.Product;
import com.example.mapper.PoItemMapper;
import com.example.repository.OrderRepo;
import com.example.repository.PoItemRepo;
import com.example.repository.ProductRepo;
import com.example.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class PoItemService {
    @Autowired
    PoItemRepo poItemRepo;
    @Autowired
    PoItemMapper poItemMapper;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    ProductRepo productRepo;
    public ResponseEntity<ApiResponse<List<PoItemDto>>> getAlPoItems() {
        try {
            List<PoItemDto> list = poItemRepo.findAll().stream()
                    .map(poItemMapper::toDto)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success("Po fetched", list));
        } catch (Exception e) {
            log.error("get all poitems failed", e);
            return ResponseEntity.internalServerError().body(ApiResponse.fail("Failed to get poitems"));
        }
    }
    public ResponseEntity<ApiResponse<PoItemDto>> getByIdPoItem(Long id) {
        try {
            PoItem poItem = poItemRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Poitem not found"));
            return ResponseEntity.ok(ApiResponse.success("Poitem fetched", poItemMapper.toDto(poItem)));
        } catch (Exception e) {
            log.error("get poitem failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<PoItemDto>> deletePoItem(Long id) {
        try {
            if (!poItemRepo.existsById(id)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.fail("poitem not found"));
            }
            poItemRepo.deleteById(id);
            return ResponseEntity.ok(
                    ApiResponse.success("poitem deleted succesfully"));
        } catch (Exception e) {
            log.error("Delete poitem failed", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Failed to delete poitem"));
        }
    }
    public ResponseEntity<ApiResponse<PoItemDto>> updatePoItem(PoItemDto dto, Long id) {
        try {
            PoItem existing = poItemRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Poitem not found"));
            poItemMapper.update(dto, existing);
            PoItem saved = poItemRepo.save(existing);
            return ResponseEntity.ok(ApiResponse.success("Poitem updated", poItemMapper.toDto(saved)));
        } catch (Exception e) {
            log.error("Update poitem failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
}