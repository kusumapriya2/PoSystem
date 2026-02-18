package com.example.controller;

import com.example.dto.DeliveryDetailsDto;
import com.example.entity.DeliveryDetails;
import com.example.response.ApiResponse;
import com.example.service.DeliveryDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryDetailsController {

    private final DeliveryDetailsService deliveryDetailsService;
    @PutMapping("/deliveries/assign/{orderId}")
    public DeliveryDetails assign(@PathVariable Long orderId) {
        return deliveryDetailsService.assignEntityByOrderIdOrThrow(orderId);
    }
    @GetMapping("/deliveries/{orderId}")
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> getByOrderId(@PathVariable Long orderId) {
        return deliveryDetailsService.getByOrderId(orderId);
    }
    @GetMapping("/allDeliveries")
    public ResponseEntity<ApiResponse<List<DeliveryDetailsDto>>> getAllDeliveries() {
        return deliveryDetailsService.getAllDeliveries();
    }

}

