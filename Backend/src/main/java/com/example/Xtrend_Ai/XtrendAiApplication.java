package com.example.Xtrend_Ai;

import com.example.Xtrend_Ai.Aws.S3Config;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class XtrendAiApplication {

	public static void main(String[] args) {

		SpringApplication.run(XtrendAiApplication.class, args);


	}


}
