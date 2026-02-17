package com.example.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
@Data
public class VendorDto {
    private Long vendorId;
    @NotBlank(message = "Vendor name cannot be blank")
    @Size(min = 2, max = 100, message = "Vendor name must be between 2 and 100 characters")
    private String vendorName;
    @NotBlank(message = "Vendor email cannot be blank")
    @Email(message = "Invalid email format")
    private String vendorEmail;
    @NotBlank(message = "GST number cannot be blank")
    private String gstNo;
    @NotBlank(message = "Vendor phone cannot be blank")
    private String vendorPhone;
    private List<Long> products;
    private List<Long> orders;
}
