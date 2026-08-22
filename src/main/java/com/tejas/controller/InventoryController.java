package com.tejas.controller;

import com.tejas.model.Inventory;
import com.tejas.payload.dto.InventoryDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.repository.InventoryRepository;
import com.tejas.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryDto> create(@RequestBody InventoryDto inventoryDto) {
        InventoryDto result = inventoryService.createInventory(inventoryDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDto> update(@PathVariable Long id, @RequestBody InventoryDto inventoryDto) {
        InventoryDto result = inventoryService.updateInventory(id, inventoryDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Thanos snapped. The inventory was reduced to atoms.");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryDto>> getInventoryByBranch(@PathVariable Long branchId) {
        List<InventoryDto> inventories = inventoryService.getAllInventoryByBranchId(branchId);
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}/branch/{branchId}")
    public ResponseEntity<InventoryDto> getInventoryByProductIdAndBranchId(@PathVariable Long productId, @PathVariable Long branchId) {
        InventoryDto inventory = inventoryService.getInventoryByProductIdAndBranchId(productId, branchId);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }
}
