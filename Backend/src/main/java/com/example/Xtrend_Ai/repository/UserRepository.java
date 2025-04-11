package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
