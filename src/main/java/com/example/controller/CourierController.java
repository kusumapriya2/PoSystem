package com.example.controller;

import com.example.dto.CourierDto;
import com.example.entity.Courier;
import com.example.response.ApiResponse;
import com.example.service.CourierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/couriers")
public class CourierController {

    @Autowired
    CourierService courierService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CourierDto>> createCourier(@Valid @RequestBody CourierDto dto) {
        return courierService.createCourier(dto);
    }

    @GetMapping("/{courierId}")
    public ResponseEntity<ApiResponse<CourierDto>> getCourierById(@PathVariable Long courierId){
        return courierService.getCourierById(courierId);
    }
    @GetMapping("/getAllCouriers")
    public ResponseEntity<ApiResponse<List<CourierDto>>> getCouriers(){
        return courierService.getCouriers();
    }
    @PutMapping("/updateCourier/{courierId}")
    public ResponseEntity<ApiResponse<CourierDto>> updateCourierId(
            @PathVariable Long courierId,
            @RequestBody CourierDto dto){
        return courierService.updateCourierById(courierId, dto);
    }
    @DeleteMapping("/deleteCourierById/{courierId}")
    public ResponseEntity<ApiResponse<CourierDto>> deleteCourierById(@PathVariable Long courierId){
        return courierService.deleteCourierById(courierId);
    }

}
