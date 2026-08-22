package com.tejas.controller;

import com.tejas.domain.UserRole;
import com.tejas.payload.dto.UserDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/store/{storeId}")
    public ResponseEntity<UserDto> createStoreEmployee(@PathVariable Long storeId,
                                                       @RequestBody UserDto userDto) {
        UserDto user = employeeService.createStoreEmployee(storeId, userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/branch/{branchId}")
    public ResponseEntity<UserDto> createBranchEmployee(@PathVariable Long branchId,
                                                        @RequestBody UserDto userDto) {
        UserDto user = employeeService.createBranchEmployee(branchId, userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateEmployee(@PathVariable Long id,
                                                  @RequestBody UserDto userDto) {
        UserDto result = employeeService.updateEmployee(id, userDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMapping(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Employee deleted!");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserDto>> findEmployeeByStore(@PathVariable Long storeId,
                                                       @RequestParam(required = false)UserRole userRole) {
        List<UserDto> users = employeeService.findEmployeesByStore(storeId, userRole);
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<UserDto>> findEmployeeByBranch(@PathVariable Long branchId,
                                                             @RequestParam(required = false)UserRole userRole) {
        List<UserDto> users = employeeService.findEmployeesByBranch(branchId, userRole);
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }
}
