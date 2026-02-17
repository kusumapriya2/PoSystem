package com.example.service;
import com.example.dto.InvoiceDTO;
import com.example.entity.Invoice;
import com.example.entity.Order;
import com.example.mapper.InvoiceMapper;
import com.example.repository.InvoiceRepo;
import com.example.repository.OrderRepo;
import com.example.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class InvoiceService {
    @Autowired
    private InvoiceRepo invoiceRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    InvoiceMapper invoiceMapper;
    public ResponseEntity<ApiResponse<String>> createInvoice(InvoiceDTO invoiceDTO) {
        try {
            if (invoiceDTO.getOrderId() == null) {
                throw new RuntimeException("orderId is required");
            }
            Order order = orderRepo.findById(invoiceDTO.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found: " + invoiceDTO.getOrderId()));
            if (invoiceRepo.existsByOrder_OrderId(order.getOrderId())) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.fail("Invoice already exists for orderId: " + order.getOrderId()));
            }
            Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
            invoice.setInvoiceId(null);
            invoice.setOrder(order);
            Invoice saved = invoiceRepo.save(invoice);
            return ResponseEntity.ok(ApiResponse.success("Invoice created: " + saved.getInvoiceId()));
        } catch (Exception e) {
            log.error("Create invoice failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }
}

