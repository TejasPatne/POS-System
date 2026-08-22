package com.tejas.service.impl;

import com.tejas.domain.UserRole;
import com.tejas.mapper.UserMapper;
import com.tejas.model.Branch;
import com.tejas.model.Store;
import com.tejas.model.User;
import com.tejas.payload.dto.UserDto;
import com.tejas.repository.BranchRepository;
import com.tejas.repository.StoreRepository;
import com.tejas.repository.UserRepository;
import com.tejas.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createStoreEmployee(Long storeId, UserDto employee) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("store not found"));
        Branch branch = null;

        if (employee.getRole() == UserRole.ROLE_BRANCH_MANAGER) {
            if (employee.getBranchId() == null) {
                throw new RuntimeException("Branch Id is required to create branch manager");
            }
            branch = branchRepository.findById(employee.getBranchId()).orElseThrow(() -> new RuntimeException("Branch not found"));
        }
        User user = UserMapper.toEntity(employee);
        user.setStore(store);
        user.setBranch(branch);
        user.setPassword(passwordEncoder.encode(employee.getPassword()));

        User savedEmployee = userRepository.save(user);
        if (employee.getRole() == UserRole.ROLE_BRANCH_MANAGER && branch != null) {
            branch.setManager(savedEmployee);
            branchRepository.save(branch);
        }
        return UserMapper.toDto(savedEmployee);
    }

    @Override
    public UserDto createBranchEmployee(Long branchId, UserDto employee) {
        Branch branch = branchRepository.findById(employee.getBranchId()).orElseThrow(() -> new RuntimeException("Branch not found"));

        if (employee.getRole() == UserRole.ROLE_BRANCH_CASHIER ||
                employee.getRole() == UserRole.ROLE_BRANCH_MANAGER) {
            User user = UserMapper.toEntity(employee);
            user.setBranch(branch);
            user.setPassword(passwordEncoder.encode(employee.getPassword()));
            return UserMapper.toDto(user);
        }
        throw new RuntimeException("branch role not supported");
    }

    @Override
    public UserDto updateEmployee(Long employeeId, UserDto employee) {
        User existingEmployee = userRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("user not found"));
        Branch branch = branchRepository.findById(employee.getBranchId()).orElseThrow(() -> new RuntimeException("branch not found"));

        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setFullName(employee.getFullName());
        existingEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        existingEmployee.setRole(employee.getRole());
        existingEmployee.setBranch(branch);
        return UserMapper.toDto(userRepository.save(existingEmployee));
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        User existingUser = userRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("user not found"));
        userRepository.delete(existingUser);
    }

    @Override
    public List<UserDto> findEmployeesByStore(Long storeId, UserRole userRole) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("store not found"));
        List<User> employees = userRepository.findByStore(store);
        return employees.stream()
                .filter(emp -> userRole == null || emp.getRole() == userRole)
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findEmployeesByBranch(Long branchId, UserRole userRole) {
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("branch not found"));
        List<User> employees = userRepository.findByBranchId(branch.getId());
        return employees.stream()
                .filter(emp -> userRole == null || emp.getRole() == userRole)
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
