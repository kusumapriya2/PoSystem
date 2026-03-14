package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;
    private String invoiceName;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private String invoiceDescription;
    private invoiceStatus invoiceStatus;
    public enum invoiceStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
