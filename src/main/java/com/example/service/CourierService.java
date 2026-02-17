package com.example.service;

import com.example.dto.AdminDto;
import com.example.dto.CourierDto;
import com.example.entity.Admin;
import com.example.entity.Courier;
import com.example.mapper.CourierMapper;
import com.example.repository.CourierRepo;
import com.example.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierService {

    private final CourierRepo courierRepo;
private final CourierMapper courierMapper;
    public ResponseEntity<ApiResponse<CourierDto>> createCourier(CourierDto dto) {
        try {
            Courier courier=courierMapper.toEntity(dto);
            Courier saved = courierRepo.save(courier);
            CourierDto sdto=courierMapper.toDto(courier);
            return ResponseEntity.ok(ApiResponse.success("Courier created", (sdto)));
        } catch (Exception e) {
            log.error("create failed",e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<CourierDto>> getCourierById(Long courierId) {
        try {
            Courier courier = courierRepo.findById(courierId)
                    .orElseThrow(() -> new RuntimeException("Courier not found"));
            CourierDto sdto=courierMapper.toDto(courier);

            return ResponseEntity.ok(ApiResponse.success("Courier found", (sdto)));

        } catch (Exception e) {
            log.error("Get courier failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<List<CourierDto>>> getCouriers() {
        try {
            List<CourierDto> list = courierRepo.findAll()
                    .stream()
                    .map(courierMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("All couriers", list));

        } catch (Exception e) {
            log.error("Get couriers failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    public ResponseEntity<ApiResponse<CourierDto>> updateCourierById(Long courierId, CourierDto dto) {
        try {
            Courier courier = courierRepo.findById(courierId)
                    .orElseThrow(() -> new RuntimeException("Courier not found"));
            if (dto.getName() != null) courier.setName(dto.getName());
            if (dto.getStatus() != null) courier.setStatus(dto.getStatus());
            if (dto.getPriority() != null) courier.setPriority(dto.getPriority());
            if (dto.getSlaDays() != null) courier.setSlaDays(dto.getSlaDays());
            Courier saved = courierRepo.save(courier);
            return ResponseEntity.ok(ApiResponse.success("Courier updated"));
        } catch (Exception e) {
            log.error("Update courier failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<CourierDto>> deleteCourierById(Long courierId) {
        try{
            Courier courier=courierRepo.findById(courierId)
                    .orElseThrow(()-> new RuntimeException("Courier not found"));
            courierRepo.deleteById(courierId);
            return ResponseEntity.ok(ApiResponse.success("courier deleted successfully",courierMapper.toDto(courier)));
        } catch (Exception e) {
            log.error("Delete courier failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
}
