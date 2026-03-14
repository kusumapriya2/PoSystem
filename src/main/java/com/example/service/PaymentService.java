package com.example.service;

import com.example.dto.PaymentDTO;
import com.example.entity.Order;
import com.example.entity.Payment;
import com.example.mapper.PaymentMapper;
import com.example.repository.OrderRepo;
import com.example.repository.PaymentRepo;
import com.example.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final OrderRepo orderRepo;
    private final PaymentMapper paymentMapper;

    public ResponseEntity<ApiResponse<PaymentDTO>> createPayment(PaymentDTO dto) {
        try {
            if (dto.getOrderId() == null) throw new RuntimeException("orderId is required");
            if (dto.getAmountPaid() == null || dto.getAmountPaid() <= 0) throw new RuntimeException("amountPaid must be > 0");
            if (dto.getPaymentMethod() == null) throw new RuntimeException("paymentMethod is required");

            Order order = orderRepo.findById(dto.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found: " + dto.getOrderId()));

            if (paymentRepo.existsByOrder_OrderId(dto.getOrderId())) {
                Payment existing = paymentRepo.findByOrder_OrderId(dto.getOrderId()).orElse(null);
                return ResponseEntity.badRequest().body(
                        ApiResponse.fail("Payment already exists for this orderId: " + dto.getOrderId()
                                + (existing != null ? " (paymentId=" + existing.getPaymentId() + ")" : ""))
                );
            }

            Payment payment = paymentMapper.toEntity(dto);
            payment.setOrder(order);
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());

            Payment saved = paymentRepo.save(payment);
            BigDecimal currentPaid = order.getAmountPaid() == null ? BigDecimal.ZERO : order.getAmountPaid();
            BigDecimal addPaid = BigDecimal.valueOf(dto.getAmountPaid());
            order.setAmountPaid(currentPaid.add(addPaid));
            orderRepo.save(order);
            return ResponseEntity.ok(ApiResponse.success("Payment created", paymentMapper.toDto(saved)));
        } catch (Exception e) {
            log.error("Payment creation failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("Payment creation failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<PaymentDTO>> getAllPayments() {
        try{
        List<Order> list=orderRepo.findAll()
                .stream()
                .toList();
        return ResponseEntity.ok(ApiResponse.success("Fetched payments", (PaymentDTO) list));
    } catch (Exception e) {
            log.error("getAllPayments failed", e);
            return ResponseEntity.ok(ApiResponse.fail("getAllPayments failed: " + e.getMessage()));
        }
        }

    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentById(Long paymentId) {
        try{
            Payment payment=paymentRepo.findById(paymentId)
                    .orElseThrow(()->new RuntimeException("failed to fetch"));
            PaymentDTO paymentDTO=paymentMapper.toDto(payment);
            return ResponseEntity.ok(ApiResponse.success("Payment found", paymentDTO));
        } catch (Exception e) {
            log.error("getPaymentById failed", e);
            return ResponseEntity.ok(ApiResponse.fail("getPaymentById failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<PaymentDTO>> updatePaymentMethod(PaymentDTO paymentDTO, long paymentMethod) {
        try{
        Payment payment=paymentRepo.findById(paymentMethod)
                .orElseThrow(()->new RuntimeException("not found"));
        PaymentDTO paymentDTO1=paymentMapper.toDto(payment);
        return ResponseEntity.ok(ApiResponse.success("updated successfully",paymentDTO1));
    }
        catch (Exception e) {
        log.error("updatePaymentMethod failed", e);
        return ResponseEntity.ok(ApiResponse.fail("updatePaymentMethod failed: " + e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<PaymentDTO>> deletePaymentById(long paymentId) {
        try{
            Payment payment=paymentRepo.findById(paymentId)
                    .orElseThrow(()->new RuntimeException("not found"));
            PaymentDTO paymentDTO=paymentMapper.toDto(payment);
            return ResponseEntity.ok(ApiResponse.success("deleted successfully",paymentDTO));
        }
        catch (Exception e) {
            log.error("deletePaymentById failed", e);
            return ResponseEntity.ok(ApiResponse.fail("deletePaymentById failed: " + e.getMessage()));
        }
    }
}
