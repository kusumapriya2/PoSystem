package com.example.repository;

import com.example.entity.DeliveryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryDetailsRepo extends JpaRepository<DeliveryDetails,Long> {

    Optional<DeliveryDetails> findByOrder_OrderId(Long orderId);

    boolean existsByOrder_OrderId(Long orderId);
}
