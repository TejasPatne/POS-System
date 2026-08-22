package com.tejas.mapper;

import com.tejas.model.Branch;
import com.tejas.model.Inventory;
import com.tejas.model.Product;
import com.tejas.payload.dto.InventoryDto;

public class InventoryMapper {
    public static InventoryDto toDto(Inventory inventory) {
        return InventoryDto.builder()
                .id(inventory.getId())
                .branchId(inventory.getBranch().getId())
                .productId(inventory.getProduct().getId())
                .product(ProductMapper.toDto(inventory.getProduct()))
                .quantity(inventory.getQuantity())
                .build();
    }

    public static Inventory toEntity(InventoryDto inventoryDto,
                                     Branch branch,
                                     Product product) {
        return Inventory.builder()
                .id(inventoryDto.getId())
                .branch(branch)
                .product(product)
                .quantity(inventoryDto.getQuantity())
                .build();
    }
}
