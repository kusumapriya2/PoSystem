package com.example.service;
import com.example.dto.ProductDTO;
import com.example.entity.Product;
import com.example.entity.Vendor;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepo;
import com.example.repository.VendorRepo;
import com.example.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final VendorRepo vendorRepo;
    private final ProductMapper productMapper;
    private final ProductRepo productRepo;
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(ProductDTO dto) {
        try {
            log.info("Creating product for vendorId={}", dto.getVendorId());
            Vendor vendor = vendorRepo.findById(dto.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + dto.getVendorId()));
            Product product = productMapper.toEntity(dto);
            product.setVendor(vendor);
            Product saved = productRepo.save(product);
            return ResponseEntity.ok(ApiResponse.success("Product created", productMapper.toDto(saved)));
        } catch (Exception e) {
            log.error("Create product failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        try {
            List<ProductDTO> list = productRepo.findAll()
                    .stream()
                    .map(productMapper::toDto)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success("Products fetched", list));
        } catch (Exception e) {
            log.error("Get all products failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<ProductDTO>> getById(Long productId) {
        try {
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
            return ResponseEntity.ok(ApiResponse.success("Product fetched", productMapper.toDto(product)));
        } catch (Exception e) {
            log.error("Get product failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<String>> deleteProduct(Long id) {
        try {
            Product product = productRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
            productRepo.delete(product);
            return ResponseEntity.ok(ApiResponse.success("Product deleted", "Deleted id: " + id));
        } catch (Exception e) {
            log.error("Delete product failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(Long id, ProductDTO dto) {
        try {
            Product existing = productRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
            existing.setProductName(dto.getProductName());
            existing.setProductType(dto.getProductType());
            existing.setProductStock(dto.getProductStock());
            existing.setProductStatus(dto.getProductStatus());
            if (dto.getVendorId() != null) {
                Vendor vendor = vendorRepo.findById(dto.getVendorId())
                        .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + dto.getVendorId()));
                existing.setVendor(vendor);
            }
            Product saved = productRepo.save(existing);
            return ResponseEntity.ok(ApiResponse.success("Product updated", productMapper.toDto(saved)));
        } catch (Exception e) {
            log.error("Update product failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProductsBySearch(String name) {

        try {

            if (name == null || name.trim().isEmpty()) {
                throw new RuntimeException("Product name is required");
            }

            List<Product> productList =
                    productRepo.findByProductNameContainingIgnoreCase(name);

            Map<Long, ProductDTO> map = new HashMap<>();

            for (Product product : productList) {
                map.put(product.getProductId(), productMapper.toDto(product));
            }

            List<ProductDTO> products = new ArrayList<>(map.values());

            products.sort(Comparator.comparing(ProductDTO::getPrice));

            return ResponseEntity.ok(
                    ApiResponse.success("Products fetched successfully", products)
            );

        } catch (Exception e) {

            log.error("Search products failed: {}", e.getMessage());

            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail(e.getMessage()));
        }
    }
}

