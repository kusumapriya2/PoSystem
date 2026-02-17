package com.example.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Data
public class Courier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courierId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, INACTIVE }

    @Column(nullable = false)
    private Integer priority = 1;

    @Column(nullable = false)
    private Integer slaDays = 3;

    @JsonIgnore
    @OneToMany(mappedBy = "courier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourierServiceArea> serviceAreas;
    @JsonIgnore
    @OneToMany(mappedBy = "courier")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "courier")
    private List<DeliveryDetails> deliveries;
}
