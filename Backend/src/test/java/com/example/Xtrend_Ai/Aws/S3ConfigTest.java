package com.example.Xtrend_Ai.Aws;

import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class S3ConfigTest {
    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Presigner s3Presigner;

    @Test
    void CheckIfS3ConnectionWorksAndBucketExists() {
        //then
        assertDoesNotThrow(()->{
            List<Bucket> buckets = s3Client.listBuckets().buckets();
            //if no exception is thrown check for buckets
            assertNotNull(buckets);
        });


    }

    @Test
    void s3Presigner() {
        //when
        assertNotNull(s3Presigner);

    }
}