package com.example.conferences.service;

import com.example.conferences.exceptions.EmailTakenException;
import com.example.conferences.exceptions.LoginTakenException;
import com.example.conferences.model.User;
import com.example.conferences.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createNewUser(User user) {
        Optional<User> userOptional = userRepository.findIfLoginTaken(user.getLogin(), user.getEmail());
        if(userOptional.isPresent())
            throw new LoginTakenException();

        userOptional = userRepository.findIfEmailTaken(user.getLogin(), user.getEmail());
        if(userOptional.isPresent())
            throw new EmailTakenException();

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean ifUserExists(User user) {
        return userRepository.findIfExists(user.getLogin(), user.getEmail())
                .isPresent();
    }

    public Optional<User> getUserById(Long lid) {
        return userRepository.findById(lid);
    }

    public Optional<User> getUserByLoginAndEmail(String login, String email) {
        return userRepository.findIfExists(login, email);
    }
    public ResponseEntity<User> updateUserEmail(Long uid, String email) {
        Optional<User> userOptional = userRepository.findById(uid);
        if(userOptional.isPresent()) {
            User user = userOptional.get();

            userOptional = userRepository.findIfEmailTaken(user.getLogin(), email);
            if(userOptional.isPresent())
                throw new EmailTakenException();

            user.setEmail(email);

            return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
