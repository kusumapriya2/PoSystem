package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByProductNameContainingIgnoreCase(String name);
    @Query
            ("SELECT p.productName, COUNT(p) FROM Product p GROUP BY p.productName ORDER BY COUNT(p) ASC")
    List<Object[]> countProductsByName();


}
