package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CourierServiceArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;
    @ManyToOne
    @JoinColumn(name = "courier_courier_id")
    private Courier courier;

    private Long pincode;



}
