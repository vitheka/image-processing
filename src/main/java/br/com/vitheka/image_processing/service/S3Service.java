package br.com.vitheka.image_processing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3Service {

    private final Logger log = LoggerFactory.getLogger(S3Service.class);

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void createBucket(String bucketName) {
        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        try {
            s3Client.createBucket(createBucketRequest);
            log.info("Bucket created: {}", createBucketRequest.bucket());
        } catch (S3Exception e) {
            log.error("Error creating bucket: {}", e.awsErrorDetails().errorMessage());
        }
    }

}
