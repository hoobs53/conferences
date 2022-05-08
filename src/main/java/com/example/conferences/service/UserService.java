package com.example.conferences.service;

import com.example.conferences.exceptions.LoginTakenException;
import com.example.conferences.model.Lecture;
import com.example.conferences.model.User;
import com.example.conferences.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createNewUser(User user) {
        Optional<User> userOptional = userRepository.findIfTaken(user.getLogin(), user.getEmail());
        if(userOptional.isPresent()) {
            throw new LoginTakenException();
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean ifUserExists(User user) {
        return userRepository.findIfExists(user.getLogin(), user.getEmail())
                .isPresent();
    }
}
