package com.tejas.service.impl;

import com.tejas.mapper.InventoryMapper;
import com.tejas.model.Branch;
import com.tejas.model.Inventory;
import com.tejas.model.Product;
import com.tejas.payload.dto.InventoryDto;
import com.tejas.repository.BranchRepository;
import com.tejas.repository.InventoryRepository;
import com.tejas.repository.ProductRepository;
import com.tejas.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) {
        Branch branch = branchRepository.findById(inventoryDto.getBranchId()).orElseThrow(() -> new RuntimeException("branch does not exist"));
        Product product = productRepository.findById(inventoryDto.getProductId()).orElseThrow(() -> new RuntimeException("product does not exist"));
        Inventory inventory = InventoryMapper.toEntity(inventoryDto, branch, product);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toDto(savedInventory);
    }

    @Override
    public InventoryDto updateInventory(Long id, InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventory.setQuantity(inventoryDto.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toDto(updatedInventory);
    }

    @Override
    public void deleteInventory(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventoryRepository.delete(inventory);
    }

    @Override
    public InventoryDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
        return InventoryMapper.toDto(inventory);
    }

    @Override
    public InventoryDto getInventoryByProductIdAndBranchId(Long productId, Long branchId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product does not exists"));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch does not exists"));
        Inventory inventory = inventoryRepository.findByProductIdAndBranchId(product.getId(), branch.getId());
        return InventoryMapper.toDto(inventory);
    }

    @Override
    public List<InventoryDto> getAllInventoryByBranchId(Long branchId) {
        List<Inventory> inventories = inventoryRepository.findByBranchId(branchId);
        return inventories.stream().map(InventoryMapper::toDto).collect(Collectors.toList());
    }
}
