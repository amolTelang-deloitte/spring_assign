package com.imbd.imbd.service.interfaces;

import com.imbd.imbd.entity.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseEntity<?> addUser(User user);
    ResponseEntity<?> getUserDetails(Long userId);
}
