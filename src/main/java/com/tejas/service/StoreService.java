package com.tejas.service;

import com.tejas.domain.StoreStatus;
import com.tejas.model.Store;
import com.tejas.model.User;
import com.tejas.payload.dto.StoreDto;

import java.util.List;

public interface StoreService {
    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreById(Long Id);
    List<StoreDto> getAllStores();
    Store getStoreByAdmin();
    StoreDto updateStore(Long id, StoreDto storeDto);
    void deleteStore(Long id);
    StoreDto getStoreByEmployee();

    StoreDto moderateStore(Long id, StoreStatus storeStatus);
}
