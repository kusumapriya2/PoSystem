package com.example.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;


    private String productType;

    private Long productStock;

    private Long price;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;
    @ManyToOne
    @JoinColumn(name="vendor_id")
    private Vendor vendor;

    public enum ProductStatus {
        ACTIVE,
        INACTIVE
    }

}
