package com.example.controller;

import com.example.dto.PoItemDto;
import com.example.response.ApiResponse;
import com.example.service.PoItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/poItems")
public class PoItemController {
    @Autowired PoItemService poItemService;
    @GetMapping("/getPoItems")
    public ResponseEntity<ApiResponse<List<PoItemDto>>> getAllPoItems(){
        return poItemService.getAlPoItems();
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<PoItemDto>> getByIdPoItem(@PathVariable Long id){
        return poItemService.getByIdPoItem(id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<PoItemDto>> deletePoItem(@PathVariable Long id){
        return poItemService.deletePoItem(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<PoItemDto>> updatePoItem(@RequestBody PoItemDto dto,@PathVariable Long id){
        return poItemService.updatePoItem(dto,id);
    }
}
