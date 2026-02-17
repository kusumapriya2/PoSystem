package com.example.controller;
import com.example.dto.DeliveryDetailsDto;
import com.example.dto.OrderDto;
import com.example.response.ApiResponse;
import com.example.service.DeliveryDetailsService;
import com.example.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired OrderService orderService;
    @Autowired
    DeliveryDetailsService deliveryDetailsService;

    @PostMapping("/createOrders")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto dto){
        return orderService.createOrders(dto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders(){

        return orderService.getAllOrders();
    }
    @PutMapping("/updateOrderById/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder( @RequestBody OrderDto dto,@PathVariable Long orderId){
        return orderService.updateOrderById(dto,orderId);
    }
    @GetMapping("/getByIdOrder/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getByIdOrder(@PathVariable Long id){
        return orderService.getByIdOrder(id);
    }
    @PutMapping("/deliveries/{orderId}/ship")
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> ship(@RequestBody DeliveryDetailsDto deliveryDetailsDto,@PathVariable Long orderId){
        return deliveryDetailsService.shipByOrderId(orderId,deliveryDetailsDto);
    }
    @PutMapping("/deliveries/{orderId}/inTransit")
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> inTransit(@PathVariable Long orderId,@RequestBody DeliveryDetailsDto deliveryDetailsDto){
        return deliveryDetailsService.transitByOrderId(orderId,deliveryDetailsDto);

    }
    @PutMapping("/deliveries/{orderId}/deliver")
    public ResponseEntity<ApiResponse<DeliveryDetailsDto>> deliver(@RequestBody DeliveryDetailsDto deliveryDetailsDto,@PathVariable Long orderId){
        return deliveryDetailsService.deliverByOrderId(orderId,deliveryDetailsDto);
    }
    @DeleteMapping("/deleteByid/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> deleteByid(@PathVariable Long orderId){
        return orderService.deleteOrderById(orderId);
    }
}
