package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class DeliveryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", nullable = false)
    private Courier courier;

    @Column(name = "tracking_number", nullable = false, unique = true)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.ASSIGNED;

    @Column(nullable = false)
    private LocalDate etaDate;

    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    @Column(nullable = false, length = 500)
    private String addressSnapshot;
    private LocalDate deliveryDate;
    public enum DeliveryStatus {
        ASSIGNED,
        SHIPPED,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED,
        PENDING,
        READYFORPICKUP
    }
}
