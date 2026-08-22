package com.tejas.service;

import com.tejas.domain.UserRole;
import com.tejas.payload.dto.UserDto;

import java.util.List;

public interface EmployeeService {
    UserDto createStoreEmployee(Long storeId, UserDto employee);
    UserDto createBranchEmployee(Long branchId, UserDto employee);
    UserDto updateEmployee(Long employeeId, UserDto employee);
    void deleteEmployee(Long employeeId);
    List<UserDto> findEmployeesByStore(Long storeId, UserRole userRole);
    List<UserDto> findEmployeesByBranch(Long branchId, UserRole userRole);
}
