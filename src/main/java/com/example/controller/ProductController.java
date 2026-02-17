package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.response.ApiResponse;
import com.example.service.ProductService;
import com.example.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired ProductService productService;
    @Autowired VendorService vendorService;

    @GetMapping("/getByIdProducts/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getByProduct(@Valid @PathVariable Long id) {
        return productService.getById(id);
    }
    @GetMapping("/getAllProducts")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        return productService.getAllProducts();
    }
    @PutMapping("/updateProductById/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProductById(@PathVariable Long productId,@RequestBody ProductDTO dto){
        return productService.updateProduct(productId,dto);
    }

}
