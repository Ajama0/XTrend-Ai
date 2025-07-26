package com.example.Xtrend_Ai.Aws;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    private S3Service underTest;

    @BeforeEach
    void setUp() {
        underTest = new S3Service(s3Client, s3Presigner);
    }

    @Test
    void CheckIfUploadingObjectInBucketWorks() {
        //given
        String bucketName = "testbucket";
        String objectKey = "testobject";
        byte[] audio = new byte[8];

        //when
        underTest.putObject(bucketName, objectKey, audio);

        //because we already build a putobjectrequest inside the class underTest,we can set the types to be captured
        ArgumentCaptor<PutObjectRequest> putObjectRequestArgumentCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> requestBodyArgumentCaptor = ArgumentCaptor.forClass(RequestBody.class);


        //then
        verify(s3Client).putObject(putObjectRequestArgumentCaptor.capture(), requestBodyArgumentCaptor.capture());

        assertEquals(bucketName, putObjectRequestArgumentCaptor.getValue().bucket());
        assertEquals(objectKey, putObjectRequestArgumentCaptor.getValue().key());

    }

    @Test
    void CheckIfGetObjectIsBuilt() {
        //given
        String bucketName = "testbucket";
        String objectKey = "testobject";

        //when
        //this method only builds
        assertDoesNotThrow(()->{
            underTest.getObject(bucketName, objectKey);
        });
    }

    @Test
    void getPresignedForObject() {
    }

    @Test
    void deleteObject() {
    }
}