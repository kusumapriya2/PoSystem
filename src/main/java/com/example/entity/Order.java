package com.example.entity;

import com.example.dto.DeliveryDetailsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @PrePersist
    public void generatePoNumber() {
        if (this.poNumber == null) {
            this.poNumber = "PO-" + System.currentTimeMillis();
        }
    }

    @Column(updatable = false)
    private String poNumber;

    private LocalDate orderDate;
    private LocalDate expectedDeliveryDate;
    private BigDecimal grandTotal;
    private BigDecimal amountPaid;
    @Enumerated(EnumType.STRING)
    private POStatus poStatus;
    private Long shippingPincode;
    private String shippingAddressLine;
    private String shippingCity;
    @Enumerated(EnumType.STRING)
    private paymentMethod paymentMethod;
    public enum paymentMethod {
        UPI,
        COD,
        CREDITCARD,
        DEBITCARD,
        WALLET
    }
    private String shippingState;

    private LocalDate deliveryDate;

    public enum POStatus {
        CREATED,
        APPROVED,
        REJECTED,
        PARTIALLY_PAID,
        PAID,
        COMPLETED,
        CANCELLED,
        PENDING
    }
  @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;
@ManyToOne
@JoinColumn(name = "courier_id")
private Courier courier;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED
    }


    @OneToMany(mappedBy="purchaseOrder", cascade=CascadeType.ALL)
    private List<PoItem> items=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subTotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal shippingCharge;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal discountAmount;

    private String discountReason;
    private BigDecimal netTotal;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private DeliveryDetails deliveryDetails;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<Invoice> invoices;
}
