package com.example.Xtrend_Ai.entity;


import com.example.Xtrend_Ai.enums.Topics;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "Users")
public class User {

    /**
     * this class holds the user information, users are able to create an account
     * upon creation they will be prompted to select some categories of topics that may interest them
     * each user will be redirected to the homepage showing them a list of the latest Trending news
     */

    @SequenceGenerator(name = "users_sequence",
            sequenceName = "users_sequence",
            allocationSize = 1)

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "users_sequence"
    )

    @Id
    private Long id;

    @NonNull
    private String keyCloakId;

    @Column(name = "email_address", nullable = false)
    private String email;




    @Column(name = "Created_At")
    private LocalDateTime createdAt;


    @Column(name = "Updated_At")
    private LocalDateTime updatedAt = LocalDateTime.now();


}
