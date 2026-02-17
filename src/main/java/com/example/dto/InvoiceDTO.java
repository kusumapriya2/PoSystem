package com.example.dto;
import com.example.entity.Invoice;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class InvoiceDTO {
    private Long invoiceId;
    private String invoiceName;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private String invoiceDescription;
    private Invoice.invoiceStatus invoiceStatus;
    private Long orderId;
}
