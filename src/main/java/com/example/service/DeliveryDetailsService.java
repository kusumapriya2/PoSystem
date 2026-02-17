package com.example.service;

import com.example.dto.CourierServiceAreaDto;
import com.example.dto.DeliveryDetailsDto;
import com.example.entity.Courier;
import com.example.entity.DeliveryDetails;
import com.example.entity.Order;
import com.example.mapper.CourierServiceMapper;
import com.example.mapper.DeliveryDetailsMapper;
import com.example.repository.CourierServiceRepo;
import com.example.repository.DeliveryDetailsRepo;
import com.example.repository.OrderRepo;
import com.example.response.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryDetailsService {

    private final DeliveryDetailsRepo deliveryRepo;
    private final CourierServiceRepo courierServiceRepo;
    private final OrderRepo orderRepo;
    private final DeliveryDetailsMapper mapper;

    @Transactional
    public DeliveryDetails assignEntityByOrderIdOrThrow(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        if (order.getPoStatus() != Order.POStatus.APPROVED) {
            throw new RuntimeException("Order must be APPROVED before assigning courier");
        }
        if (deliveryRepo.existsByOrder_OrderId(orderId)) {
            return deliveryRepo.findByOrder_OrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Delivery already exists for this order"));
        }

        Long pincode = order.getShippingPincode();
        if (pincode == null) {
            throw new RuntimeException("Order shippingPincode is required");
        }

        List<Courier> eligible = courierServiceRepo.findActiveCouriersByPincode(pincode);
        if (eligible.isEmpty()) {
            throw new RuntimeException("No ACTIVE courier serves pincode: " + pincode);
        }


        Courier selected = eligible.stream()
                .sorted(Comparator
                        .comparing(Courier::getSlaDays, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(Courier::getPriority, Comparator.nullsLast(Integer::compareTo)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No eligible courier found"));

        String tracking = generateTrackingNumber();
        int sla = selected.getSlaDays() == null ? 0 : selected.getSlaDays();
        LocalDate eta = LocalDate.now().plusDays(sla);
        String snapshot = buildAddressSnapshot(order);

        DeliveryDetails delivery = mapper.create(
                order,
                selected,
                tracking,
                eta,
                DeliveryDetails.DeliveryStatus.ASSIGNED,
                snapshot
        );


        delivery.setDeliveryDate(eta);

        DeliveryDetails saved = deliveryRepo.save(delivery);

        order.setCourier(selected);
        order.setDeliveryDetails(saved);
        order.setDeliveryDate(eta);
        orderRepo.save(order);

        return saved;
    }

    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> assignByOrderId(Long orderId) {
        try {
            DeliveryDetails saved = assignEntityByOrderIdOrThrow(orderId);
            return ResponseEntity.ok(ApiResponse.success("Delivery assigned", mapper.toDto(saved)));
        } catch (Exception e) {
            log.error("Assign delivery failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }


    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> getByOrderId(Long orderId) {
        try {
            if (orderId == null) throw new RuntimeException("orderId must not be null");

            DeliveryDetails d = deliveryRepo.findByOrder_OrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Delivery not created for this order yet"));

            return ResponseEntity.ok(ApiResponse.success("Delivery details", mapper.toDto(d)));
        } catch (Exception e) {
            log.error("Get delivery failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> shipByOrderId(Long orderId, DeliveryDetailsDto deliveryDetailsDto) {
        try {
            DeliveryDetails d = deliveryRepo.findByOrder_OrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Delivery not created for this order yet"));

            if (d.getDeliveryStatus() != DeliveryDetails.DeliveryStatus.ASSIGNED) {
                throw new RuntimeException("Delivery must be ASSIGNED before SHIPPED");
            }

            d.setDeliveryStatus(DeliveryDetails.DeliveryStatus.SHIPPED);
            d.setShippedAt(LocalDateTime.now());


            if (deliveryDetailsDto != null && deliveryDetailsDto.getEtaDate() != null) {
                d.setEtaDate(deliveryDetailsDto.getEtaDate());
            }

            return ResponseEntity.ok(ApiResponse.success("Order shipped", mapper.toDto(deliveryRepo.save(d))));
        } catch (Exception e) {
            log.error("Ship failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> transitByOrderId(Long orderId, DeliveryDetailsDto deliveryDetailsDto) {
        try {
            DeliveryDetails d = deliveryRepo.findByOrder_OrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Delivery not created for this order yet"));
            if (d.getDeliveryStatus() != DeliveryDetails.DeliveryStatus.SHIPPED) {
                throw new RuntimeException("Delivery must be SHIPPED before moving to TRANSIT");
            }

            d.setDeliveryStatus(DeliveryDetails.DeliveryStatus.IN_TRANSIT);
            if (deliveryDetailsDto != null && deliveryDetailsDto.getEtaDate() != null) {
                d.setEtaDate(deliveryDetailsDto.getEtaDate());
            }

            DeliveryDetails updated = deliveryRepo.save(d);

            return ResponseEntity.ok(
                    ApiResponse.success("Delivery is now in transit", mapper.toDto(updated))
            );
        } catch (Exception e) {
            log.error("Transit failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> deliverByOrderId(Long orderId, DeliveryDetailsDto deliveryDetailsDto) {
        try {
            DeliveryDetails d = deliveryRepo.findByOrder_OrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Delivery not created for this order yet"));

            if (d.getDeliveryStatus() != DeliveryDetails.DeliveryStatus.SHIPPED
                    && d.getDeliveryStatus() != DeliveryDetails.DeliveryStatus.IN_TRANSIT) {
                throw new RuntimeException("Delivery must be SHIPPED/IN_TRANSIT before DELIVERED");
            }

            d.setDeliveryStatus(DeliveryDetails.DeliveryStatus.DELIVERED);
            d.setDeliveredAt(LocalDateTime.now());

            return ResponseEntity.ok(ApiResponse.success("Order delivered", mapper.toDto(deliveryRepo.save(d))));
        } catch (Exception e) {
            log.error("Deliver failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    private String generateTrackingNumber() {
        return "TRK-" + LocalDate.now() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String buildAddressSnapshot(Order order) {
        return order.getShippingAddressLine() + ", " +
                order.getShippingCity() + ", " +
                order.getShippingState() + " - " +
                order.getShippingPincode();
    }

    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> getAllDeliveries() {
        try{
            List<DeliveryDetailsDto> list=deliveryRepo.findAll()
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            return ResponseEntity.ok(ApiResponse.success("Fetched delivery details", (DeliveryDetailsDto) list));
        } catch (Exception e) {
            log.error("getAllDeliveries", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
}
