package com.tejas.payload.dto;

import com.tejas.domain.StoreStatus;
import com.tejas.model.StoreContact;
import com.tejas.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoreDto {
    private Long id;
    private String brand;
    private UserDto storeAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;
    private String storeType;
    private StoreStatus status;
    private StoreContact storeContact = new StoreContact();
}
