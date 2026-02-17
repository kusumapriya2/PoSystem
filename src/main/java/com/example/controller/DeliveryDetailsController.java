package com.example.controller;

import com.example.dto.DeliveryDetailsDto;
import com.example.response.ApiResponse;
import com.example.service.DeliveryDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryDetailsController {

    private final DeliveryDetailsService deliveryDetailsService;
    @PutMapping("/deliveries/assign/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> assign(@PathVariable Long orderId) {
        return deliveryDetailsService.assignByOrderId(orderId);
    }
    @GetMapping("/deliveries/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> getByOrderId(@PathVariable Long orderId) {
        return deliveryDetailsService.getByOrderId(orderId);
    }
    @GetMapping("/allDeliveries")
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> getAllDeliveries() {
        return deliveryDetailsService.getAllDeliveries();
    }

}

