package com.tejas.repository;

import com.tejas.model.Store;
import com.tejas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByStore(Store store);
    List<User> findByBranchId(Long branchId);
}
