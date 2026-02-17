package com.example.controller;

import com.example.dto.ProductDTO;
import com.example.dto.VendorDto;
import com.example.response.ApiResponse;
import com.example.service.ProductService;
import com.example.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {
    @Autowired VendorService vendorService;
    @Autowired ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<VendorDto>> create(@Valid @RequestBody VendorDto dto){
        return vendorService.createVendor(dto);
    }

    @GetMapping("/allVendors")
    public ResponseEntity<ApiResponse<List<VendorDto>>> getAllVendors(){
        return vendorService.getAllVendors();
    }

    @GetMapping("/getById/{vendorId}")
    public ResponseEntity<ApiResponse<VendorDto>> getById(@PathVariable Long vendorId){
        return vendorService.getById(vendorId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<VendorDto>> updateVendor(@RequestBody VendorDto dto,@PathVariable Long id){
        return vendorService.updateVendorById(dto,id);
    }

    @DeleteMapping("/delete/{vendorId}")
    public ResponseEntity<ApiResponse<VendorDto>> deleteVendor(@PathVariable Long vendorId){
        return vendorService.deleteVendor(vendorId);
    }

    @PostMapping("/createProducts")
    public ResponseEntity<ApiResponse<ProductDTO>> createProducts(@RequestBody ProductDTO dto){
        return productService.createProduct(dto);
    }
    @DeleteMapping("/deleteProducts/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
    }
    @PutMapping("/updateProducts/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@RequestBody ProductDTO dto, @PathVariable Long id){
        return productService.updateProduct(id,dto);
    }
}

