package br.com.vitheka.image_processing.service;

import br.com.vitheka.image_processing.domain.S3BucketObjectRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void putObject(String bucketName, S3BucketObjectRepresentation s3BucketObjectRepresentation) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3BucketObjectRepresentation.objectName())
                .build();

        try {
            Path file = Files.write(
                    Paths.get(".", s3BucketObjectRepresentation.objectName()),
                    s3BucketObjectRepresentation.text().getBytes());
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
            log.info("Object created: {}", putObjectRequest.key());
        } catch (S3Exception e) {
            log.error("Error uploading object: {}", e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
