package com.example.controller;
import com.example.dto.InvoiceDTO;
import com.example.response.ApiResponse;
import com.example.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<InvoiceDTO>> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }
    @PutMapping("/updateInvoiceById/{invoiceId}")
    public ResponseEntity<ApiResponse<InvoiceDTO>> updateInvoiceById(@RequestBody InvoiceDTO invoiceDTO, @PathVariable("invoiceId") Long invoiceId) {
        return invoiceService.updateInvoiceById(invoiceDTO,invoiceId);
    }
    @GetMapping("/getAllInvoices")
    public ResponseEntity<ApiResponse<List<InvoiceDTO>>> getAllInvoices(){
        return invoiceService.getAllInvoices();
    }
}
