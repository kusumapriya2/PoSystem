package com.example.controller;

import com.example.dto.PaymentDTO;
import com.example.response.ApiResponse;
import com.example.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/addPayment")
    public ResponseEntity<ApiResponse<PaymentDTO>> addPayment(@RequestBody PaymentDTO paymentDTO) {
        return paymentService.createPayment(paymentDTO);
    }
    @GetMapping("/getAllPayments/")
    public ResponseEntity<ApiResponse<PaymentDTO>> getAllPayments() {
        return paymentService.getAllPayments();
    }
    @GetMapping("/getPaymentById'{paymentId}")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentById(@RequestParam long paymentId) {
        return paymentService.getPaymentById(paymentId);
    }
    @PutMapping("/updatePayment/{paymentMethod}")
    public ResponseEntity<ApiResponse<PaymentDTO>> updatePayment(@PathVariable long paymentMethod, @RequestBody PaymentDTO paymentDTO) {
        return paymentService.updatePaymentMethod(paymentDTO,paymentMethod);
    }
    @DeleteMapping("/deletePayment/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentDTO>> deletePayment(@PathVariable long paymentId) {
        return paymentService.deletePaymentById(paymentId);
    }
}

