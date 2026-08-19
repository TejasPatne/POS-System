package com.tejas.service.impl;

import com.tejas.domain.StoreStatus;
import com.tejas.exceptions.UserException;
import com.tejas.mapper.StoreMapper;
import com.tejas.model.Store;
import com.tejas.model.StoreContact;
import com.tejas.model.User;
import com.tejas.payload.dto.StoreDto;
import com.tejas.repository.StoreRepository;
import com.tejas.service.StoreService;
import com.tejas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final UserService userService;

    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {
        Store store = StoreMapper.toEntity(storeDto, user);
        return StoreMapper.toDto(storeRepository.save(store));
    }

    @Override
    public StoreDto getStoreById(Long Id) {
        Store store = storeRepository.findById(Id)
                .orElseThrow(() -> new UserException("The Marauder's Map couldn't find this store."));
        return StoreMapper.toDto(store);
    }

    @Override
    public List<StoreDto> getAllStores() {
        List<Store> storeList = storeRepository.findAll();
        return storeList.stream().map(StoreMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Store getStoreByAdmin() {
        User userAdmin = userService.getCurrentUser();
        return storeRepository.findByStoreAdminId(userAdmin.getId());
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) {
        User currentUser = userService.getCurrentUser();
        Store existingStore = storeRepository.findByStoreAdminId(currentUser.getId());

        if (existingStore ==  null) {
            throw new RuntimeException("store not found");
        }

        existingStore.setBrand(storeDto.getBrand());
        existingStore.setDescription(storeDto.getDescription());

        if (storeDto.getStoreType()!=null) {
            existingStore.setStoreType(storeDto.getStoreType());
        }

        if (storeDto.getStoreContact()!=null) {
            StoreContact newContact = StoreContact.builder().address(storeDto.getStoreContact().getAddress())
                                                            .phone(storeDto.getStoreContact().getPhone())
                                                            .email(storeDto.getStoreContact().getEmail())
                                                            .build();
            existingStore.setStoreContact(newContact);
        }

        Store updatedStore = storeRepository.save(existingStore);

        return StoreMapper.toDto(updatedStore);
    }

    @Override
    public void deleteStore(Long id) {
        Store store = getStoreByAdmin();
        storeRepository.delete(store);
    }

    @Override
    public StoreDto getStoreByEmployee() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new UserException("you don't have permission to access store");
        }
        Store store = storeRepository.findById(currentUser.getStore().getId()).orElseThrow(() -> new RuntimeException("Store not found"));
        return StoreMapper.toDto(store);
    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus storeStatus) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("Store not found"));
        store.setStatus(storeStatus);
        Store updatedStore = storeRepository.save(store);
        return StoreMapper.toDto(updatedStore);
    }
}
