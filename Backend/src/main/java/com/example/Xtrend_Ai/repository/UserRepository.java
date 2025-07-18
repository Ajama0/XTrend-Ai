package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from User u WHERE u.email=?1")
    Optional<User> findByEmail(String username);
}
