package com.example.dto;

import com.example.entity.Product;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDTO {


    private Long productId;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String productName;

    @NotBlank(message = "Product type cannot be blank")
    private String productType;

    @NotNull(message = "Product stock cannot be null")
    @PositiveOrZero(message = "Product stock must be zero or positive")
    private Long productStock;

    @NotNull(message = "Product status cannot be null")
    private Product.ProductStatus productStatus;

    @NotNull(message = "Vendor ID cannot be null")
    private Long vendorId;
    private Long price;
}
