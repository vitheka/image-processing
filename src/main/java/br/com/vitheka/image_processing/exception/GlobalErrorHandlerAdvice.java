package br.com.vitheka.image_processing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ControllerAdvice
public class GlobalErrorHandlerAdvice {

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<DefaultErrorMessage> handleS3Exception(S3Exception ex) {

        var errorResponse = new DefaultErrorMessage(ex.statusCode(),
                "Error uploading image: " + ex.awsErrorDetails().errorMessage());

        return ResponseEntity.status(ex.statusCode()).body(errorResponse);
    }
}
