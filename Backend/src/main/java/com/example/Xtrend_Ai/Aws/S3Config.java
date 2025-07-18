package com.example.Xtrend_Ai.Aws;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {


    @Value("{aws.access.key}")
    private String ACCESS_KEY;

    @Value("${aws.secret.key}")
    private String SECRET_KEY;





    @Bean
    public software.amazon.awssdk.services.s3.S3Client s3Client() {
        //credentials are required because the user will access the bucket based of our credentials
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        software.amazon.awssdk.services.s3.S3Client client = software.amazon.awssdk.services.s3.S3Client.builder()
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        return client;

    }

    @Bean
    public S3Presigner s3Presigner() {
        //essentially it will use this presigner implementation we return to create a presignrequest for our object

        Region awsRegion = Region.EU_CENTRAL_1;
        return S3Presigner.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();


    }

}
