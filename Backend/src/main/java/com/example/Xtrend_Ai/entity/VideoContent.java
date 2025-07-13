//package com.example.Xtrend_Ai.entity;
//
//import com.example.Xtrend_Ai.enums.Status;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.net.URL;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@ToString
//@Builder
//public class VideoContent{
//
//
//    @SequenceGenerator(sequenceName = "videoContent_sequence",
//            name = "videoContent_sequence",
//            allocationSize = 1
//    )
//
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,
//            generator = "videoContent_sequence"
//    )
//    @Id
//    private Long id;
//
//    private String video_id;
//
//    private String videoTitle;
//
//    private String video_url;
//
//    private String video_description;
//
//    private Double duration;
//
//
//    /***
//     * each generated content will be associated with the news entity.
//     */
//    @ManyToOne
//    @JoinColumn(name = "news_id",   nullable = false)
//    private News news;
//
//    @ManyToOne
//    @JoinColumn(name="user_id")
//    private User user;
//
//    @Enumerated(EnumType.STRING)
//    private Status status;
//}
