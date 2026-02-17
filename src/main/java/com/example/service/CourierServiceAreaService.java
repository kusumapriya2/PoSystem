package com.example.service;
import com.example.dto.CourierServiceAreaDto;
import com.example.entity.Courier;
import com.example.entity.CourierServiceArea;
import com.example.mapper.CourierServiceMapper;
import com.example.repository.CourierRepo;
import com.example.repository.CourierServiceRepo;
import com.example.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CourierServiceAreaService {
    private final CourierServiceRepo areaRepo;
    private final CourierRepo courierRepo;
    private final CourierServiceMapper courierServiceMapper;
    public ResponseEntity<ApiResponse<CourierServiceAreaDto>> createServiceArea(
            Long courierId,
             CourierServiceAreaDto dto) {
        try {
            if (dto.getPincode() == null) {
                throw new RuntimeException("Pincode is required");
            }
            Courier courier = courierRepo.findById(courierId)
                    .orElseThrow(() -> new RuntimeException("Courier not found: " + courierId));
            boolean exists = areaRepo.existsByCourier_CourierIdAndPincode(courierId, dto.getPincode());
            if (exists) {
                throw new RuntimeException("Service area already exists for this courier & pincode");
            }
            CourierServiceArea area = new CourierServiceArea();
            area.setCourier(courier);
            area.setPincode(dto.getPincode());
            CourierServiceArea savedArea = areaRepo.save(area);
            return ResponseEntity.ok(ApiResponse.success("Courier service area created", courierServiceMapper.toDto(savedArea)));
        } catch (Exception e) {
            log.error("Create service area failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<List<CourierServiceAreaDto>>> createServiceAreasBulk(
            Long courierId,
             List<CourierServiceAreaDto> dtos
    ) {
        try {
            if (dtos == null || dtos.isEmpty()) {
                throw new RuntimeException("Service area list cannot be empty");
            }
            Courier courier = courierRepo.findById(courierId)
                    .orElseThrow(() -> new RuntimeException("Courier not found: " + courierId));
            List<CourierServiceArea> toSave = dtos.stream().map(dto -> {
                if (dto.getPincode() == null) throw new RuntimeException("Pincode is required");
                if (areaRepo.existsByCourier_CourierIdAndPincode(courierId, dto.getPincode())) {
                    throw new RuntimeException("Already exists for pincode: " + dto.getPincode());
                }
                CourierServiceArea area = new CourierServiceArea();
                area.setCourier(courier);
                area.setPincode(dto.getPincode());
                return area;
            }).toList();
            List<CourierServiceAreaDto> response = areaRepo.saveAll(toSave)
                    .stream().map(courierServiceMapper::toDto).toList();
            return ResponseEntity.ok(ApiResponse.success("Bulk service areas created", response));
        } catch (Exception e) {
            log.error("Bulk create service areas failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<List<CourierServiceAreaDto>>> getServiceAreasByCourierById(Long courierId) {
        try {
            List<CourierServiceArea> areas = areaRepo.findByCourier_CourierId(courierId);

            if (areas.isEmpty()) {
                throw new RuntimeException("No service areas found for courier: " + courierId);
            }
            List<CourierServiceAreaDto> dtoList = areas.stream().map(courierServiceMapper::toDto).toList();
            return ResponseEntity.ok(ApiResponse.success("Courier service areas", dtoList));
        } catch (Exception e) {
            log.error("Get service areas failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<String>> deleteServiceArea(Long serviceAreaId) {
        try {
            CourierServiceArea area = areaRepo.findById(serviceAreaId)
                    .orElseThrow(() -> new RuntimeException("Service area not found: " + serviceAreaId));
            areaRepo.delete(area);
            return ResponseEntity.ok(ApiResponse.success("Courier service area deleted", "deleted"));
        } catch (Exception e) {
            log.error("Delete service area failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<CourierServiceAreaDto>> updateCourierServiceArea(Long serviceAreaId, CourierServiceAreaDto courierServiceAreaDto) {
        try {
            CourierServiceArea existing=areaRepo.findById(serviceAreaId)
            .orElseThrow(() -> new RuntimeException("courierservice area not found"));
            courierServiceMapper.update(courierServiceAreaDto, existing);
            CourierServiceArea saved = areaRepo.save(existing);
            return ResponseEntity.ok(ApiResponse.success("Courier service area updated",courierServiceMapper.toDto(saved)));
        } catch (RuntimeException e) {
            log.error("update courier service area failed",e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<List<CourierServiceAreaDto>>> getAllCourierServiceAreas() {
        try{
            List<CourierServiceAreaDto> list=areaRepo.findAll()
                    .stream()
                    .map(courierServiceMapper::toDto)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success("fetched courier service areas successfuly",list));
        } catch (Exception e) {
            log.error("Get all courier service areas",e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
}
