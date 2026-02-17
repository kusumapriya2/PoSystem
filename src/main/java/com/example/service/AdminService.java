package com.example.service;

import com.example.dto.AdminDto;
import com.example.dto.OrderDto;
import com.example.entity.*;
import com.example.mapper.AdminMapper;
import com.example.mapper.OrderMapper;
import com.example.repository.AdminRepo;
import com.example.repository.OrderRepo;
import com.example.repository.ProductRepo;
import com.example.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepo adminRepository;
    private final AdminMapper adminMapper;

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;

    private final DeliveryDetailsService deliveryDetailsService;
    private final ProductRepo productRepo;

    public ResponseEntity<ApiResponse<AdminDto>> createAdmin(AdminDto dto) {
        try {
            if (adminRepository.count() > 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.fail("Admin already exists. Only one admin is allowed."));
            }

            Admin saved = adminRepository.save(adminMapper.toEntity(dto));
            return ResponseEntity.ok(ApiResponse.success("Admin created successfully", adminMapper.toDTO(saved)));

        } catch (Exception e) {
            log.error("Admin creation failed", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Failed to create admin: " + e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<OrderDto>> approveOrder(Long orderId) {
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            if (order.getPoStatus() == Order.POStatus.APPROVED)
                throw new RuntimeException("Order already approved");

            if (order.getItems() == null || order.getItems().isEmpty())
                throw new RuntimeException("Missing order items");

            if (order.getShippingPincode() == null
                    || order.getShippingAddressLine() == null
                    || order.getShippingCity() == null
                    || order.getShippingState() == null)
                throw new RuntimeException("Missing shipping address");

            if (order.getNetTotal() == null || order.getNetTotal().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Invalid net total");
            }


            if (order.getCustomer() == null)
                throw new RuntimeException("Missing customer");

            if (order.getVendor() == null)
                throw new RuntimeException("Missing vendor");
            if (order.getPaymentMethod() != null && order.getPaymentMethod() != Order.paymentMethod.COD) {
                BigDecimal paid = (order.getAmountPaid() == null) ? BigDecimal.ZERO : order.getAmountPaid();
                BigDecimal net = order.getNetTotal();

                if (paid.compareTo(net) < 0) {
                    return rejectOrder(order, "Order rejected: Full payment required for non-COD orders");
                }
            }
            for (PoItem item : order.getItems()) {
                if (item.getProduct() == null || item.getProduct().getProductId() == null)
                    throw new RuntimeException("Order item product missing");

                int qty = (item.getQuantity() == null) ? 0 : item.getQuantity();
                if (qty <= 0)
                    throw new RuntimeException("Invalid quantity");

                Product product = productRepo.findById(item.getProduct().getProductId())
                        .orElseThrow(() -> new RuntimeException(
                                "Product not found: " + item.getProduct().getProductId()));

                long stock = (product.getProductStock() == null) ? 0L : product.getProductStock();
                if (stock < qty)
                    throw new RuntimeException("Insufficient stock for productId=" + product.getProductId());

                product.setProductStock(stock - qty);
                productRepo.save(product);
            }
            order.setPoStatus(Order.POStatus.APPROVED);
            Order savedOrder = orderRepo.save(order);
            DeliveryDetails delivery = deliveryDetailsService.assignEntityByOrderIdOrThrow(orderId);
            delivery.setOrder(savedOrder);
            savedOrder.setDeliveryDetails(delivery);

            Order finalSaved = orderRepo.save(savedOrder);

            return ResponseEntity.ok(
                    ApiResponse.success("Order approved & courier assigned", orderMapper.toDto(finalSaved))
            );

        } catch (Exception e) {
            log.error("Approve failed", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("Approve failed: " + e.getMessage()));
        }
    }

    private ResponseEntity<ApiResponse<OrderDto>> rejectOrder(Order order, String message) {
        order.setPoStatus(Order.POStatus.REJECTED);
        orderRepo.save(order);
        return ResponseEntity.badRequest().body(ApiResponse.fail(message));
    }

    public ResponseEntity<ApiResponse<AdminDto>> updateadmin(Long adminId, AdminDto dto) {
        try {
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            Admin saved = adminRepository.save(admin);

            return ResponseEntity.ok(ApiResponse.success("Admin updated successfully", adminMapper.toDTO(saved)));
        } catch (Exception e) {
            log.error("Admin update failed", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<List<AdminDto>>> getAdmin() {
        try{
            List<AdminDto> list=adminRepository.findAll()
                    .stream()
                    .map(adminMapper::toDTO)
                    .toList();
        return ResponseEntity.ok(ApiResponse.success("fetched admin",list));
    } catch (Exception e) {
            log.error("Admin found failed", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail(e.getMessage()));
            }
        }

    public ResponseEntity<ApiResponse<AdminDto>> deleteAdmin(Long adminId) {
        try{
        Admin admin=adminRepository.findById(adminId)
                .orElseThrow(()->new RuntimeException("Admin not found"));
        adminRepository.delete(admin);
        return ResponseEntity.ok(ApiResponse.success("deleted admin",adminMapper.toDTO(admin)));
    }
    catch (Exception e) {
        log.error("Admin found failed", e);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.fail(e.getMessage()));
    }
    }

    public ResponseEntity<ApiResponse<AdminDto>> getAdminById(Long adminId) {
        try{
            Admin admin=adminRepository.findById(adminId)
                    .orElseThrow(()->new RuntimeException("Admin not found"));
            AdminDto dto=adminMapper.toDTO(admin);
            return ResponseEntity.ok(ApiResponse.success("fetched admin",dto));
        }
        catch (Exception e) {
            log.error("Admin found failed", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail(e.getMessage()));
        }
    }
}
