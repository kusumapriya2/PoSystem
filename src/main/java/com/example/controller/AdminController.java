package com.example.controller;

import com.example.dto.AdminDto;
import com.example.dto.OrderDto;
import com.example.response.ApiResponse;
import com.example.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<AdminDto>> createAdmin(@Valid @RequestBody AdminDto dto){
        return adminService.createAdmin(dto);
    }
    @PutMapping("/orders/{orderId}/approve")
    public ResponseEntity<ApiResponse<OrderDto>> updateApprove(@PathVariable Long orderId){
        return adminService.approveOrder(orderId);
    }
    @PutMapping("update/{adminId}")
    public ResponseEntity<ApiResponse<AdminDto>> updateAdmin(@PathVariable Long adminId,@RequestBody AdminDto dto){
        return adminService.updateadmin(adminId,dto);
    }
    @GetMapping("/getAdmin")
    public ResponseEntity<ApiResponse<List<AdminDto>>> getAdmin(){
        return adminService.getAdmin();
    }
    @DeleteMapping("deleteAdmin")
    public ResponseEntity<ApiResponse<AdminDto>> deleteAdmin(@RequestParam Long adminId){
        return adminService.deleteAdmin(adminId);
    }
    @GetMapping("/getAdminById/{adminId}")
    public ResponseEntity<ApiResponse<AdminDto>> getAdminById(@PathVariable Long adminId){
        return adminService.getAdminById(adminId);
    }
}
