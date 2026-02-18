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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceService {
    @Autowired
    private InvoiceRepo invoiceRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    InvoiceMapper invoiceMapper;

    public ResponseEntity<ApiResponse<InvoiceDTO>> updateInvoiceById(InvoiceDTO invoiceDTO, Long invoiceId) {
        try{
            Invoice invoice=invoiceRepo.findById(invoiceId)
                    .orElseThrow(()->new RuntimeException("invoice not found"));
            Invoice savedInvoice=invoiceRepo.save(invoice);
            return ResponseEntity.ok(ApiResponse.success("invoice updated successfully",invoiceMapper.toDto(savedInvoice)));
        }
        catch(Exception e){
            log.error("failed to update",e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<InvoiceDTO>> createInvoice(InvoiceDTO invoiceDTO) {
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
            return ResponseEntity.ok(ApiResponse.success("Invoice created: " ,invoiceMapper.toDto(invoice)));
        } catch (Exception e) {
            log.error("Create invoice failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse<List<InvoiceDTO>>> getAllInvoices() {
        try{
        List<InvoiceDTO> invoices=invoiceRepo.findAll()
                .stream()
                .map(invoiceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("invoices found", invoices));
    } catch (Exception e) {
            log.error("getAllInvoices failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        }
        }

}

