package com.tejas.service.impl;

import com.tejas.mapper.BranchMapper;
import com.tejas.model.Branch;
import com.tejas.model.Store;
import com.tejas.model.User;
import com.tejas.payload.dto.BranchDto;
import com.tejas.repository.BranchRepository;
import com.tejas.repository.StoreRepository;
import com.tejas.service.BranchService;
import com.tejas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;

    @Override
    public BranchDto createBranch(BranchDto branchDto) {
        User currentUser = userService.getCurrentUser();
        Store store = storeRepository.findByStoreAdminId(currentUser.getId());
        Branch branch = BranchMapper.toEntity(branchDto, store);
        Branch savedBranch = branchRepository.save(branch);
        return BranchMapper.toDto(savedBranch);
    }

    @Override
    public BranchDto updateBranch(Long id, BranchDto branchDto) {
        Branch existing = branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch does not exist"));
        existing.setName(branchDto.getName());
        existing.setWorkingDays(branchDto.getWorkingDays());
        existing.setEmail(branchDto.getEmail());
        existing.setAddress(branchDto.getAddress());
        existing.setPhone(branchDto.getPhone());
        existing.setEmail(branchDto.getEmail());
        existing.setOpenTime(branchDto.getOpenTime());
        existing.setCloseTime(branchDto.getCloseTime());
        existing.setUpdatedAt(branchDto.getUpdatedAt());

        Branch updatedBranch = branchRepository.save(existing);
        return BranchMapper.toDto(updatedBranch);
    }

    @Override
    public void deleteBranch(Long id) {
        Branch existing = branchRepository.findById(id).orElseThrow(() -> new RuntimeException("branch not found"));
        branchRepository.delete(existing);
    }

    @Override
    public List<BranchDto> getAllBranchesByStoreId(Long storeId) {
        List<Branch> branches = branchRepository.findByStoreId(storeId);
        return branches.stream().map(BranchMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BranchDto getBranchById(Long id) {
        Branch existing = branchRepository.findById(id).orElseThrow(() -> new RuntimeException("branch not found"));
        return BranchMapper.toDto(existing);
    }
}
