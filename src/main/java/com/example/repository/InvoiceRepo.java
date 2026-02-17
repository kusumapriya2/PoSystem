package com.example.repository;
import com.example.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    boolean existsByOrder_OrderId(Long orderId);
}
