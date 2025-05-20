package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.client.HeyGen.HeyGenClient;
import com.example.Xtrend_Ai.dto.*;
import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.entity.User;
import com.example.Xtrend_Ai.entity.VideoContent;
import com.example.Xtrend_Ai.enums.Status;
import com.example.Xtrend_Ai.repository.NewsRepository;
import com.example.Xtrend_Ai.repository.UserRepository;
import com.example.Xtrend_Ai.repository.VideoContentRepository;
import com.example.Xtrend_Ai.utils.Dimension;
import com.example.Xtrend_Ai.utils.VideoInputs;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoService {
    private final HeyGenClient heyGenClient;
    private final ObjectMapper objectMapper;
    private final NewsRepository newsRepository;
    private final DiffBotService diffBotService;
    private final GPTService gptService;
    private final UserRepository userRepository;
    private final VideoContentRepository videoContentRepository;


    @Value("${voices.id}")
    private String voicesId;


    @Value("${avatars.id}")
    private String avatarsId;

    public AvatarResponse fetchAvatars() {
        try {
            Response response = heyGenClient.ListOfAvatars();
            if (response.code() == 200) {
                assert response.body() != null;
                String json = response.body().string();
                AvatarResponse avatarResponse = objectMapper.readValue(json, AvatarResponse.class);
                return avatarResponse;
            }else{
                throw new RuntimeException("a status code other than 200 returned" + response.code());
            }
        }catch (IOException e) {
            throw new RuntimeException("error occurred processing avatar response", e);
        }

    }

    public VoiceResponse fetchVoices() {
        try {
            Response response = heyGenClient.ListOfVoices();
            if (response.code() == 200) {
                VoiceResponse voiceResponse = objectMapper.readValue(response.body().string(), VoiceResponse.class);
                return voiceResponse;
            }else{
                throw new RuntimeException("a status code other than 200 returned" + response.code());
            }
        }catch (IOException e) {
            throw new RuntimeException("error occurred processing avatar response", e);
        }

    }

    public HeyGenResponse generateVideo(Long id, String username) throws IOException {
        /***
         * the workflow of generating a video consists of passing in the id of the trending news and the user to generate content
         * after validating the news object we pass the url to the DiffBot API which uses ML to scrape article content
         * the content is fed into our LLM to generate a video script
         * this video script is then passed into HeyGen's input text property.
         *
         */
        //TODO : the width is 1280 and height is 720 for dimensions
        Optional<News> news = newsRepository.findById(id);
        Optional<User> users = userRepository.findByUsername(username);

        if (news.isEmpty() || users.isEmpty()) {
            throw new RuntimeException("There is no news with id " + id);
        }

        News fetchedNews = news.get();
        User fetchedUser = users.get();
        String extractedContent = diffBotService.extractTextFromArticle(fetchedNews.getArticle().getUrl());
        String videoScript = gptService.getGptResponse(extractedContent,null,true);

        VideoInputs.Voice voice = VideoInputs.Voice.builder()
                .type("avatar")
                .inputText(videoScript)
                .voice_id(voicesId)
                .speed(1.0)
                .build();

        VideoInputs.Character character = VideoInputs.Character.builder()
                .type("avatar")
                .avatar_id(avatarsId)
                .avatar_style("normal")
                .build();



        VideoInputs.Background background = VideoInputs.Background
                .builder()
                .type("color")
                .value("#FAFAFA")
                .build();


        VideoInputs videoInputs = VideoInputs.builder()
                .voice(voice)
                .character(character)
                .background(background)
                .build();

        Dimension dimension = Dimension
                .builder()
                .width(1280)
                .height(720)
                .build();

        HeyGenRequest heyGenRequest = HeyGenRequest
                .builder()
                .title(fetchedNews.getArticle().getTitle())
                .videoInputs(List.of(videoInputs))
                .dimension(dimension)
                .callbackUrl("")
                .caption(true)
                .build();

        /// we can now pass our heygen payload to the our client library
        HeyGenResponse heyGenResponse = new HeyGenResponse();
        Response response = heyGenClient.GenerateVideo(heyGenRequest);
        if (response.code() == 200 && response.body() != null) {
           heyGenResponse = objectMapper.readValue(response.body().string(), HeyGenResponse.class);
         /// using a callback this uses a webhook to send the video and status to our post request when video is generated
        /// we can save the video id to check the status later on
        }

        /// we can save the video generated by the user with the status of pending.
        /// a call back will be triggered to our post request when the video is generated
        VideoContent videoContent = VideoContent
                .builder()
                .video_id(heyGenResponse.getVideoResponse().getVideo_id())
                .videoTitle(fetchedNews.getArticle().getTitle())
                .news(fetchedNews)
                .user(fetchedUser)
                .video_url("") /// default
                .duration(10.00F) /// default
                .status(Status.PENDING)
                .build();
        videoContentRepository.save(videoContent);


        }


    /**
     * we recieve a callback from heygen which includes the generated video including the status
     * @param heyGenWebhook
     * @return
     */
    public String RecieveVideo(HeyGenWebhook heyGenWebhook) {
        /// where video_id == id from heygen
        VideoContent getVideo = videoContentRepository.findVideoContentByVideoId(
                heyGenWebhook.getWebhookData().getId()).orElseThrow(()-> new RuntimeException("video record was not found"));



        log.info("payload: {}", heyGenWebhook.getWebhookData());
        String videoUrl = heyGenWebhook.getWebhookData().getVideo_url();
        String status = heyGenWebhook.getWebhookData().getStatus();

        if(status == "completed" && !videoUrl.isEmpty()){
            /// update our record by persisting the video url and updating the status
            getVideo.setVideo_url(videoUrl);
            getVideo.setStatus(Status.COMPLETED);
            getVideo.setDuration(heyGenWebhook.getWebhookData().getDuration());
            videoContentRepository.save(getVideo);

            return videoUrl;
        }else{
            throw new RuntimeException("video status was either not completed or video was empty");
        }


    }
}


