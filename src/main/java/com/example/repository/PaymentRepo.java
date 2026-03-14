package com.example.repository;
import com.example.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrder_OrderId(Long orderId);

    boolean existsByOrder_OrderId(Long orderId);
}
