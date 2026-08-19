package com.tejas.controller;

import com.tejas.domain.StoreStatus;
import com.tejas.mapper.StoreMapper;
import com.tejas.model.User;
import com.tejas.payload.dto.StoreDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.service.StoreService;
import com.tejas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto, @RequestHeader("Authorization") String jwt) {
        User user = userService.getUserFromJwtToken(jwt);
        return new ResponseEntity<>(storeService.createStore(storeDto, user), HttpStatus.CREATED);
    }

    @GetMapping("/admin")
    public ResponseEntity<StoreDto> getStoreByAdmin() {
        return new ResponseEntity<>(StoreMapper.toDto(storeService.getStoreByAdmin()), HttpStatus.OK);
    }

    @GetMapping("/employee")
    public ResponseEntity<StoreDto> getStoreByEmployee() {
        return new ResponseEntity<>(storeService.getStoreByEmployee(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllStores() {
        return new ResponseEntity<>(storeService.getAllStores(), HttpStatus.OK);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long Id) {
        return new ResponseEntity<>(storeService.getStoreById(Id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        return new ResponseEntity<>(storeService.updateStore(id, storeDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Store deleted successfully!");
        return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/moderate")
    public ResponseEntity<StoreDto> moderateStore(@PathVariable Long id, @RequestParam StoreStatus storeStatus) {
        return new ResponseEntity<>(storeService.moderateStore(id, storeStatus), HttpStatus.OK);
    }
}
