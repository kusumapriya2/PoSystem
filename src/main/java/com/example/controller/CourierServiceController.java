package com.example.controller;

import com.example.dto.CourierServiceAreaDto;
import com.example.entity.CourierServiceArea;
import com.example.response.ApiResponse;
import com.example.service.CourierServiceAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/courierService")
public class CourierServiceController {

    private final CourierServiceAreaService courierServiceAreaService;
    @PostMapping("/add/{courierId}")
    public ResponseEntity<ApiResponse<CourierServiceAreaDto>> createCourierServiceArea(
            @PathVariable Long courierId,
            @Valid @RequestBody CourierServiceAreaDto dto) {
        return courierServiceAreaService.createServiceArea(courierId, dto);
    }
    @PostMapping("/addBulk/{courierId}")
    public ResponseEntity<ApiResponse<List<CourierServiceAreaDto>>> createCourierServiceAreasBulk(
            @PathVariable Long courierId,
            @Valid @RequestBody List<CourierServiceAreaDto> dtos) {
        return courierServiceAreaService.createServiceAreasBulk(courierId, dtos);
    }

    @GetMapping("/{courierId}")
    public ResponseEntity<ApiResponse<List<CourierServiceAreaDto>>> getCourierServiceAreas(
            @PathVariable Long courierId) {
        return courierServiceAreaService.getServiceAreasByCourierById(courierId);
    }

    @DeleteMapping("/deleteAreaId/{areaId}")
    public ResponseEntity<ApiResponse<String>> deleteCourierServiceArea(@PathVariable Long areaId) {
        return courierServiceAreaService.deleteServiceArea(areaId);
    }
    @PutMapping("/updateCourierService/{serviceAreaId}")
    public ResponseEntity<ApiResponse<CourierServiceAreaDto>> updateCourierServiceArea(@PathVariable Long serviceAreaId,@RequestBody CourierServiceAreaDto courierServiceAreaDto){
        return courierServiceAreaService.updateCourierServiceArea(serviceAreaId,courierServiceAreaDto);
    }
    @GetMapping("/getAllCourierServiceAreas")
    public ResponseEntity<ApiResponse<List<CourierServiceAreaDto>>> getAllCourierServiceAreas(){
        return courierServiceAreaService.getAllCourierServiceAreas();
    }
}
