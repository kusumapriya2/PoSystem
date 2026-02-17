package com.example.repository;

import com.example.entity.PoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoItemRepo extends JpaRepository<PoItem,Long> {
}
