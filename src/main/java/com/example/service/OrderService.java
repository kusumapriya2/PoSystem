package com.example.service;

import com.example.dto.OrderDto;
import com.example.dto.PoItemDto;
import com.example.entity.*;
import com.example.mapper.OrderMapper;
import com.example.repository.CustomerRepo;
import com.example.repository.OrderRepo;
import com.example.repository.ProductRepo;
import com.example.repository.VendorRepo;
import com.example.response.ApiResponse;
import com.example.utils.EmailTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private VendorRepo vendorRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<ApiResponse<OrderDto>> createOrders(OrderDto dto) {
        try {
            if (dto.getCustomerId() == null) throw new RuntimeException("CustomerId is required");
            if (dto.getVendorId() == null) throw new RuntimeException("VendorId is required");
            if (dto.getItems() == null || dto.getItems().isEmpty()) throw new RuntimeException("Order items required");
            if (dto.getShippingPincode() == null) throw new RuntimeException("Shipping pincode is required for courier assignment");
            if (dto.getPaymentMethod() == null) throw new RuntimeException("Payment method is required");

            Customer customer = customerRepo.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + dto.getCustomerId()));

            Vendor vendor = vendorRepo.findById(dto.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found: " + dto.getVendorId()));

            boolean isNewCustomer = !orderRepo.existsByCustomer_CustomerId(dto.getCustomerId());
            Order order = orderMapper.toEntity(dto);
            order.setCustomer(customer);
            order.setVendor(vendor);
            order.setOrderDate(LocalDate.now());
            order.setPoStatus(Order.POStatus.CREATED);
            order.setPaymentMethod(dto.getPaymentMethod());
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
            BigDecimal subTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            List<PoItem> items = new ArrayList<>();

            for (PoItemDto itemDto : dto.getItems()) {
                if (itemDto.getProductId() == null) throw new RuntimeException("ProductId is required in items");
                if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0)
                    throw new RuntimeException("Quantity must be > 0 for productId=" + itemDto.getProductId());

                Product product = productRepo.findById(itemDto.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.getProductId()));

                BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal qty = BigDecimal.valueOf(itemDto.getQuantity());
                BigDecimal lineTotal = unitPrice.multiply(qty).setScale(2, RoundingMode.HALF_UP);

                PoItem item = new PoItem();
                item.setPurchaseOrder(order);
                item.setProduct(product);
                item.setQuantity(itemDto.getQuantity());
                item.setPrice(unitPrice.doubleValue());
                item.setAmount(lineTotal.doubleValue());
                subTotal = subTotal.add(lineTotal).setScale(2, RoundingMode.HALF_UP);
                items.add(item);
            }

            order.setItems(items);
            BigDecimal taxAmount = subTotal.multiply(BigDecimal.valueOf(0.18))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal shippingCharge = calculateShipping(subTotal, dto.getShippingPincode())
                    .setScale(2, RoundingMode.HALF_UP);
            LocalTime t = LocalDateTime.now().toLocalTime();

            BigDecimal discountAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            StringBuilder reason = new StringBuilder();
            boolean happyHour = !t.isBefore(LocalTime.of(16, 7)) && t.isBefore(LocalTime.of(21, 0));
            if (happyHour) {
                BigDecimal hh = subTotal.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
                discountAmount = discountAmount.add(hh).setScale(2, RoundingMode.HALF_UP);
                reason.append("HAPPY_HOUR(10%)");
            }

            if (isNewCustomer) {
                BigDecimal fivePercent = subTotal.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal newCustDisc = fivePercent.min(BigDecimal.valueOf(200.00)).setScale(2, RoundingMode.HALF_UP);
                discountAmount = discountAmount.add(newCustDisc).setScale(2, RoundingMode.HALF_UP);
                if (reason.length() > 0) reason.append(" + ");
                reason.append("NEW_CUSTOMER");
            }

            BigDecimal maxDiscount = subTotal.multiply(BigDecimal.valueOf(0.20)).setScale(2, RoundingMode.HALF_UP);
            if (discountAmount.compareTo(maxDiscount) > 0) discountAmount = maxDiscount;

            discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);
            BigDecimal grandTotal = subTotal.add(taxAmount).add(shippingCharge)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal netTotal = grandTotal.subtract(discountAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            if (netTotal.compareTo(BigDecimal.ZERO) < 0) {
                netTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }
            order.setSubTotal(subTotal);
            order.setTaxAmount(taxAmount);
            order.setShippingCharge(shippingCharge);
            order.setGrandTotal(grandTotal);
            order.setNetTotal(netTotal);

            order.setDiscountAmount(discountAmount);
            order.setDiscountReason(reason.length() == 0 ? "Discount not Applicable anymore" : reason.toString());

            if (order.getAmountPaid() == null) {
                order.setAmountPaid(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            }

            Order saved = orderRepo.save(order);

            emailService.sendEmail(
                    saved.getCustomer().getEmail(),
                    "Order Placed - " + saved.getPoNumber(),
                    EmailTemplates.orderPlaced(saved)
            );
            return ResponseEntity.ok(ApiResponse.success("Order created", orderMapper.toDto(saved)));

        } catch (Exception e) {
            log.error("Create order failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    private BigDecimal calculateShipping(BigDecimal subTotal, Long pincode) {
        if (subTotal.compareTo(BigDecimal.valueOf(50000)) >= 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(150.00);
    }

    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        try {
            List<OrderDto> list = orderRepo.findAll().stream()
                    .map(order -> orderMapper.toDto(order))
                    .toList();

            return ResponseEntity.ok(ApiResponse.success("orders fetched", list));
        } catch (Exception e) {
            log.error("Get all orders failed", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Failed to fetch orders"));
        }
    }

    public ResponseEntity<ApiResponse<OrderDto>> getByIdOrder(Long id) {
        try {
            Order order = orderRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("order not found"));

            return ResponseEntity.ok(ApiResponse.success("order fetched", orderMapper.toDto(order)));
        } catch (Exception e) {
            log.error("order fetch failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }


    public ResponseEntity<ApiResponse<OrderDto>> updateOrderById(OrderDto dto, Long orderId) {
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("order not found"));

            orderMapper.updateOrder(dto, order);

            Order saved = orderRepo.save(order);
            return ResponseEntity.ok(ApiResponse.success("order updated", orderMapper.toDto(saved)));
        } catch (Exception e) {
            log.error("update order failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<OrderDto>> deleteOrderById(Long orderId) {
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("order not found"));

            orderRepo.delete(order);
            return ResponseEntity.ok(ApiResponse.success("order deleted", orderMapper.toDto(order)));
        } catch (Exception e) {
            log.error("delete order failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
}
