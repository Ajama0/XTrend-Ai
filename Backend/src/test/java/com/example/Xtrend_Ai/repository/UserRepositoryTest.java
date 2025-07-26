package com.example.Xtrend_Ai.repository;

import com.example.Xtrend_Ai.entity.Podcast;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.enums.Topics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    void CheckIfUserEmailExists() {
        //given
        User user = User
                .builder()
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .firstname("John")
                .lastname("Smith")
                .password("testing123")
                .interests(List.of(Topics.SPORTS, Topics.GAMING))
                .build();

        userRepository.save(user);

        //when
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        //then
        assertTrue(userOptional.isPresent());

    }


    @Test
    void CheckIfUserEmailDoesNotExist() {
        //given
        String email = "example@example.com";

        //when
        Optional<User> userOptional = userRepository.findByEmail(email);

        assertFalse(userOptional.isPresent());
    }

    @Test
    void DatabaseConnection(){
        assertEquals(List.of(), userRepository.findAll());

    }



}