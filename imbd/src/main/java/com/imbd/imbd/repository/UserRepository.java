package com.imbd.imbd.repository;

import com.imbd.imbd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByemail(String email);
}
