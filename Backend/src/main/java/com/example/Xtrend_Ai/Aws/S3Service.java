package com.example.Xtrend_Ai.Aws;


import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;


    /***
     * the aws sdk is used to upload audio files to our s3 bucket
     * @param bucketName - where all objects are stored
     * @param objectKey - exact location of audio
     * @param audio - podcast audio stream
     */
    public void putObject(String bucketName, String objectKey, byte[] audio) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromBytes(audio));

    }



}
