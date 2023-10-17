package com.imbd.imbd.service;

import com.imbd.imbd.dto.ResponseDTO;
import com.imbd.imbd.entity.User;
import com.imbd.imbd.exception.customExceptions.InvalidUserException;
import com.imbd.imbd.repository.UserRepository;
import com.imbd.imbd.service.interfaces.IDTOBuilderService;
import com.imbd.imbd.service.interfaces.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements IUserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final IDTOBuilderService dtoBuilderService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRepository userRepository1, IDTOBuilderService dtoBuilderService){
        this.userRepository = userRepository1;
        this.dtoBuilderService = dtoBuilderService;
    }

    @Override
    public ResponseEntity<?> addUser(User user) {
        if(userRepository.existsByemail(user.getEmail()))
        {
            logger.warn("User with the email '{}' already exists in the database at {}.", user.getEmail(), LocalDateTime.now());
            throw new InvalidUserException("Email already exists !");
        }
        userRepository.save(user);
        logger.info("User added into database at {}", LocalDateTime.now());
        ResponseDTO<User> responseDTO = dtoBuilderService.createResponse(user,HttpStatus.CREATED,"New user Details.");
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> getUserDetails(Long userId) {
        if(!userRepository.existsById(userId))
        {
            logger.error("User with Id '{}' does not exist at {}.", userId, LocalDateTime.now());
            throw new IllegalArgumentException("Invalid User Id !.");
        }
        User user = userRepository.findById(userId).get();
        logger.info("User with Id '{}' successfully retrived from database at {}",userId,LocalDateTime.now());
        ResponseDTO<User> responseDTO = dtoBuilderService.createResponse(user,HttpStatus.OK,"Used Details.");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
