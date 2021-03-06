package com.example.conferences.repository;

import com.example.conferences.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT s FROM User s WHERE s.email = ?1")
    Optional<User> findByEmail(String email);
    @Query("SELECT s FROM User s WHERE s.login = ?1")
    Optional<User> findByLogin(String login);
    @Query("SELECT s FROM User s WHERE s.login = ?1 AND s.email != ?2")
    Optional<User> findIfLoginTaken(String login, String email);
    @Query("SELECT s FROM User s WHERE s.login != ?1 AND s.email = ?2")
    Optional<User> findIfEmailTaken(String login, String email);
    @Query("SELECT s FROM User s WHERE s.login = ?1 AND s.email = ?2")
    Optional<User> findIfExists(String login, String email);
}
