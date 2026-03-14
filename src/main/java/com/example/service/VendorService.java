package com.example.service;
import com.example.dto.VendorDto;
import com.example.entity.Vendor;
import com.example.mapper.VendorMapper;
import com.example.repository.VendorRepo;
import com.example.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class VendorService {
    @Autowired
    VendorRepo vendorRepo;
    @Autowired
    VendorMapper vendorMapper;
    public ResponseEntity<ApiResponse<VendorDto>> createVendor(VendorDto dto) {
        try{
            Vendor saved=vendorRepo.save(vendorMapper.toEntity(dto));
            return ResponseEntity.ok(ApiResponse.success("Vendor Created",vendorMapper.toDTO(saved)));
        } catch (Exception e) {
            log.error("Create vendor failed",e);
            return ResponseEntity.internalServerError().body(ApiResponse.fail("Failed to create vendor"));
        }
    }
    public ResponseEntity<ApiResponse<List<VendorDto>>> getAllVendors() {
        try{
            List<VendorDto> list=vendorRepo.findAll().stream()
                    .map(vendorMapper::toDTO)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success("Vendors fetched",list));
        } catch (Exception e) {
            log.error("Get all vendors failed",e);
            return ResponseEntity.internalServerError().body(ApiResponse.fail("Failed to fetch vendor"));
        }
    }
    public ResponseEntity<ApiResponse<VendorDto>> getById(Long vendorId) {
        try{
            Vendor vendor=vendorRepo.findById(vendorId)
                    .orElseThrow(()->new RuntimeException("failed"));
return ResponseEntity.ok(ApiResponse.success("Vendor fetched",vendorMapper.toDTO(vendor)));
        } catch (Exception e) {
            log.error("Get vendor by id failed",e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<VendorDto>> updateVendorById(VendorDto dto,Long vendorId)
    {try{
        Vendor existing=vendorRepo.findById(vendorId)
                .orElseThrow(()-> new RuntimeException("Vendor not found"));
        vendorMapper.updateVendorFromDto(dto,existing);
        Vendor saved=vendorRepo.save(existing);
        return ResponseEntity.ok(ApiResponse.success("Vendor updated",vendorMapper.toDTO(saved)));
    } catch (Exception e) {
        log.error("Update vendor failed",e);
        return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
    }
    }
    public ResponseEntity<ApiResponse<VendorDto>> deleteVendor(Long id) {
        try{
            if(!vendorRepo.existsById(id)){
                return ResponseEntity.badRequest()
                        .body(ApiResponse.fail("Vendor not found"));
            }
            vendorRepo.deleteById(id);
            return ResponseEntity.ok(
                    ApiResponse.success("Vendor deleted succesfully")
            );
        } catch (Exception e) {
            log.error("Delete vendor failed",e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Failed to delete vendor"));
        }
    }
}
