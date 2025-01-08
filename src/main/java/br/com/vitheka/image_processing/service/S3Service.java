package br.com.vitheka.image_processing.service;


import br.com.vitheka.image_processing.exception.*;
import br.com.vitheka.image_processing.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;
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
    private final LambdaClient lambdaClient;


    public S3Service(S3Client s3Client, LambdaClient lambdaClient) {
        this.s3Client = s3Client;
        this.lambdaClient = lambdaClient;
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

    public void putObject(String bucketName, S3BucketObjectRepresentation  s3BucketObjectRepresentation) {
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

    public void putImage(String bucketName, String objectName, byte[] imageBytes) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .contentType("image/jpeg")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(imageBytes));
            log.info("Imagem enviada para o bucket S3 com sucesso!");

            invokeLambdaFunction(bucketName, objectName);

        } catch (S3Exception e) {
            throw new ImageUploadException(
                    String.format("Erro ao enviar a imagem '%s' para o bucket '%s': %s", objectName, bucketName, e.awsErrorDetails().errorMessage())
            );
        } catch (SdkClientException e) {
            throw new ConnectProblemException(String.format("Erro ao conectar: '%s'", e.getMessage()));
        }
    }

    private void invokeLambdaFunction(String bucketName, String objectName) {
        try {
            String payload = String.format("{\"bucketName\": \"%s\", \"objectName\": \"%s\"}", bucketName, objectName);

            InvokeRequest request = InvokeRequest.builder()
                    .functionName("nome-da-sua-lambda-function")
                    .payload(SdkBytes.fromUtf8String(payload))
                    .build();

            InvokeResponse response = lambdaClient.invoke(request);
            String responsePayload = response.payload().asUtf8String();
            log.info("Resposta da Lambda: {}", responsePayload);
        } catch (LambdaException e) {
            log.error("Erro ao invocar a função Lambda: {}", e.getMessage());
            throw new RuntimeException("Erro ao invocar a função Lambda", e);
        }
    }
}
