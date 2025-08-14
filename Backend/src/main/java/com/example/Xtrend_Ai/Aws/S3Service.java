package com.example.Xtrend_Ai.Aws;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;


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



    public GetObjectRequest getObject(String bucket, String key) {
        return GetObjectRequest
                .builder()
                .bucket(bucket)
                .key(key)
                .build();

        //this is our object that we use retrieve the file by passing in our bucket and key
    }

    /**
     *
     * @param bucket - location of stored objects
     * @param key - audio byte location
     * - use Presigned urls which are generated from aws that allow users to access the audio stream directly from our bucket
     * - however the credentials to our bucket are hidden
     * - instead of returning bytes from the image recieved from AWS..
     * every time a user makes a request we generate a signed url
     * url expiration time is set to 7 days, however we will refresh this every 6th day and override the db.
     *
     * @return presigned URL
     */

    public URL getPresignedForObject(String bucket, String key) {

        GetObjectRequest objectRequest = getObject(bucket, key);
        // you can change expiration time here
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60 * 24 * 7))
                .getObjectRequest(objectRequest)
                .build();

        //the presigned bean, uses an implementation to generate a url for our object request
        //this returns us a presigned object based on our request which includes the actual file and the expiration
        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);
        return presigned.url();

    }


    public void DeleteObject(String bucket, String key) {

        try{
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);

        }catch(AwsServiceException awsServiceException){
            throw new RuntimeException("error occurred whilst deleting object", awsServiceException);
        }


    }





}
