package com.example.repository;

import com.example.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierRepo extends JpaRepository<Courier,Long> {
}
