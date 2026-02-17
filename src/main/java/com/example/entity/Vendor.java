package com.example.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
    private String gstNo;
    private String  vendorPhone;
    @OneToMany(mappedBy = "vendor")
    private List<Product> products = new ArrayList<>();
    @OneToMany(mappedBy = "vendor")
    private List<Order> orders = new ArrayList<>();
}
