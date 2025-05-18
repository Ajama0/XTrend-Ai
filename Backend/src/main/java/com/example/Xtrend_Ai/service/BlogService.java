package com.example.Xtrend_Ai.service;

import com.example.Xtrend_Ai.entity.News;
import com.example.Xtrend_Ai.exceptions.ArticleNotFoundException;
import com.example.Xtrend_Ai.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {

    private final NewsRepository newsRepository;
    private final DiffBotService diffBotService;
    private final GPTService gptService;


    /**
     * @param id  - the id of the article the user wants to generate a blog from
     * @param url - url pointing to the exact article which allows us to extract content
     * @return text - represents extracted textual content from the url
     */
    public String generateBlog(Long id, String url, String username) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isEmpty()) {
            throw new ArticleNotFoundException("no Articles were found");
        }
        News retrieveNews = news.get();

        if (!retrieveNews.getArticle().getUrl().equals(url)) {
            throw new RuntimeException("urls do not match");
        }


        /// once a user is ready to generate their blog, we can pass it to the diffBot serivce to extract content
        /// Call Diffbot service
        String extractedText = diffBotService.extractTextFromArticle(url);
        log.info("extracted texted");

        /// we can then pass the article content to GPTService alongside the username to generate the blog.
        String blog = gptService.getGptResponse(extractedText,username);
        return blog;




    }
}
