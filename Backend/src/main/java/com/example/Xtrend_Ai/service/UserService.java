package com.example.Xtrend_Ai.service;


import com.example.Xtrend_Ai.exceptions.UsernameNotFoundException;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;



    public User login(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found"));
    }

    public void ensureUserProvisioned(String keyCloakId, String email) {
        Optional<User> user = userRepository.findByKeyCloakId(keyCloakId);

        if(user.isPresent()){
            User foundUser = user.get();
            if(!foundUser.getEmail().equals(email)) {
                foundUser.setEmail(email);
                userRepository.save(foundUser);
            }
        }

        else{
            User newUser = User.builder()
                    .email(email)
                    .keyCloakId(keyCloakId)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(newUser);

        }


    }
}
