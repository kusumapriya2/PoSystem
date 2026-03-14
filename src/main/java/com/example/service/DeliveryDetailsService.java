package com.example.service;
import com.example.dto.DeliveryDetailsDto;
import com.example.entity.Courier;
import com.example.entity.CourierServiceArea;
import com.example.entity.DeliveryDetails;
import com.example.entity.Order;
import com.example.mapper.DeliveryDetailsMapper;
import com.example.repository.CourierServiceRepo;
import com.example.repository.DeliveryDetailsRepo;
import com.example.repository.OrderRepo;
import com.example.response.ApiResponse;
import com.example.utils.EmailTemplates;
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
    private final CourierServiceRepo courierRepo;
    private final DeliveryDetailsMapper deliveryDetailsMapper;
    private final OrderRepo orderRepo;
    private final DeliveryDetailsMapper mapper;
    private final EmailService emailService;

    @Transactional
    public DeliveryDetails assignEntityByOrderIdOrThrow(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // ✅ Rule A: only APPROVED orders can get courier
        if (order.getPoStatus() != Order.POStatus.APPROVED) {
            throw new RuntimeException("Courier can be assigned only for APPROVED orders. Current: " + order.getPoStatus());
        }

        // ✅ Rule E: if already assigned, return existing delivery
        DeliveryDetails existing = deliveryRepo.findByOrder_OrderId(orderId).orElse(null);
        if (existing != null) return existing;

        if (order.getShippingPincode() == null) {
            throw new RuntimeException("Shipping pincode missing");
        }

        Long pincode = order.getShippingPincode();

        // ✅ Rule B + C: find ACTIVE couriers who serve this pincode
        // This depends on your repo. Most common:
        // courierServiceAreaRepo.findByPincode(pincode) -> returns list of CourierServiceArea
        List<CourierServiceArea> areas = courierRepo.findByPincode(pincode);

        if (areas == null || areas.isEmpty()) {
            throw new RuntimeException("No courier service available for pincode: " + pincode);
        }

        // Convert areas -> couriers and filter ACTIVE
        List<Courier> eligibleCouriers = areas.stream()
                .map(CourierServiceArea::getCourier)
                .filter(c -> c != null && c.getStatus() == Courier.Status.ACTIVE)
                .distinct()
                .toList();

        if (eligibleCouriers.isEmpty()) {
            throw new RuntimeException("No ACTIVE couriers available for pincode: " + pincode);
        }

        // ✅ Rule D: choose best courier
        // Simple best selection: priority ASC, slaDays ASC
        // (optional) add load balancing if you have method: deliveryRepo.countByCourierAndStatusIn(...)
        Courier best = eligibleCouriers.stream()
                .sorted((c1, c2) -> {
                    int p = Integer.compare(c1.getPriority(), c2.getPriority());
                    if (p != 0) return p;
                    return Integer.compare(c1.getSlaDays(), c2.getSlaDays());
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No suitable courier found"));

        // ✅ Create DeliveryDetails with PENDING
        DeliveryDetails dd = new DeliveryDetails();
        dd.setOrder(order);
        dd.setCourier(best);
        dd.setDeliveryStatus(DeliveryDetails.DeliveryStatus.PENDING);

        // address snapshot (if you have column)
        dd.setAddressSnapshot(order.getShippingAddressLine() + ", " + order.getShippingCity()
                + ", " + order.getShippingState() + " - " + order.getShippingPincode());

        // tracking number simple dummy
        dd.setTrackingNumber("TRK-" + order.getPoNumber());

        // ETA = today + slaDays
        dd.setEtaDate(LocalDate.now().plusDays(best.getSlaDays()));

        DeliveryDetails saved = deliveryRepo.save(dd);

        // Optional: attach to order (if your mapping requires)
        order.setDeliveryDetails(saved);
        orderRepo.save(order);

        return saved;
    }


    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> shipByOrderId(Long orderId, DeliveryDetailsDto dto) {
        try {
            DeliveryDetails d = deliveryRepo.findByOrder_OrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Delivery not created for this order yet"));

            if (d.getDeliveryStatus() != DeliveryDetails.DeliveryStatus.ASSIGNED) {
                throw new RuntimeException("Delivery must be ASSIGNED before SHIPPED");
            }

            d.setDeliveryStatus(DeliveryDetails.DeliveryStatus.SHIPPED);
            d.setShippedAt(LocalDateTime.now());

            if (dto != null && dto.getEtaDate() != null) {
                d.setEtaDate(dto.getEtaDate());
            }

            DeliveryDetails savedDD = deliveryRepo.save(d);

            emailService.sendEmail(
                    savedDD.getOrder().getCustomer().getEmail(),
                    "Order Shipped - " + savedDD.getOrder().getPoNumber(),
                    EmailTemplates.shipped(savedDD.getOrder(), savedDD)
            );

            return ResponseEntity.ok(ApiResponse.success("Order shipped", mapper.toDto(savedDD)));

        } catch (Exception e) {
            log.error("Ship failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> transitByOrderId(Long orderId, DeliveryDetailsDto dto) {
        try {
            DeliveryDetails d = deliveryRepo.findByOrder_OrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Delivery not created for this order yet"));

            if (d.getDeliveryStatus() != DeliveryDetails.DeliveryStatus.SHIPPED) {
                throw new RuntimeException("Delivery must be SHIPPED before moving to IN_TRANSIT");
            }

            d.setDeliveryStatus(DeliveryDetails.DeliveryStatus.IN_TRANSIT);

            if (dto != null && dto.getEtaDate() != null) {
                d.setEtaDate(dto.getEtaDate());
            }

            DeliveryDetails savedDD = deliveryRepo.save(d);

            emailService.sendEmail(
                    savedDD.getOrder().getCustomer().getEmail(),
                    "Order In Transit - " + savedDD.getOrder().getPoNumber(),
                    EmailTemplates.inTransit(savedDD.getOrder(), savedDD)
            );

            return ResponseEntity.ok(ApiResponse.success("Delivery is now in transit", mapper.toDto(savedDD)));

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

            DeliveryDetails savedDD = deliveryRepo.save(d);

            emailService.sendEmail(
                    savedDD.getOrder().getCustomer().getEmail(),
                    "Order Delivered - " + savedDD.getOrder().getPoNumber(),
                    EmailTemplates.delivered(savedDD.getOrder(), savedDD)
            );

            return ResponseEntity.ok(ApiResponse.success("Order delivered", mapper.toDto(savedDD)));

        } catch (Exception e) {
            log.error("Deliver failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<List<DeliveryDetailsDto>>> getAllDeliveries() {
        try {
            List<DeliveryDetailsDto> list = deliveryRepo.findAll()
                    .stream()
                    .map(mapper::toDto)
                    .toList();

            return ResponseEntity.ok(ApiResponse.success("Fetched delivery details", list));
        } catch (Exception e) {
            log.error("getAllDeliveries failed", e);
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

    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> getByOrderId(Long orderId) {
        try{
        DeliveryDetails saved=deliveryRepo.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Delivery not created for this order yet"));
        DeliveryDetailsDto deliveryDetailsDto = mapper.toDto(saved);
        return ResponseEntity.ok(ApiResponse.success("fetched delivery details successfully", mapper.toDto(saved)));
    }
        catch (Exception e){
        log.error("getByOrderId failed", e);
        return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
}
