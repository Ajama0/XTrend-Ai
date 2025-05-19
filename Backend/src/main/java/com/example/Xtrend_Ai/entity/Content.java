//package com.example.Xtrend_Ai.entity;
//
//
//import com.example.Xtrend_Ai.enums.Types;
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.LocalDateTime;
//
//@Inheritance(strategy = InheritanceType.JOINED)
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
//public abstract class Content {
//
//    /**
//     * NewsContent and VideoContentRepository represent the generated content associated with a user.
//     */
//    @SequenceGenerator(sequenceName = "content_sequence",
//            name = "content_sequence",
//            allocationSize = 1
//    )
//
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,
//        generator = "content_sequence"
//    )
//
//    @Id
//    private Long id;
//
//    @Column(nullable = false, length = 100)
//    private String title;
//
//    @Column(nullable = false, name = "created_at")
//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    /// allows us to define the content type for convenient querying later
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Types type;
//
//}
