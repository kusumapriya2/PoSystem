package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class PoItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long poItemId;

        @ManyToOne
        @JoinColumn(name="purchase_order_id")
        private Order purchaseOrder;
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name="product_id")
        private Product product;

        private Integer quantity;
        private Double price;
        private Double amount;
        private BigDecimal unitPrice;
        private BigDecimal lineTotal;

    }