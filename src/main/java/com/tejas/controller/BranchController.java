package com.tejas.controller;

import com.tejas.payload.dto.BranchDto;
import com.tejas.payload.response.ApiResponse;
import com.tejas.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {
    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchDto> createBranch(@RequestBody BranchDto branchDto) {
        BranchDto createdBranch = branchService.createBranch(branchDto);
        return new ResponseEntity<>(createdBranch, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable Long id) {
        BranchDto branch = branchService.getBranchById(id);
        return new ResponseEntity<>(branch, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDto> updateBranch(@PathVariable Long id,
                                                  @RequestBody BranchDto branchDto) {
        BranchDto branch = branchService.updateBranch(id, branchDto);
        return new ResponseEntity<>(branch, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Branch was pruned and transported into the void");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
