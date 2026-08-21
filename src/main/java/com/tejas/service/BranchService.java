package com.tejas.service;

import com.tejas.payload.dto.BranchDto;

import java.util.List;

public interface BranchService {
    BranchDto createBranch(BranchDto branchDto);

    BranchDto updateBranch(Long id, BranchDto branchDto);

    void deleteBranch(Long id);
    List<BranchDto> getAllBranchesByStoreId(Long storeId);
    BranchDto getBranchById(Long id);
}
