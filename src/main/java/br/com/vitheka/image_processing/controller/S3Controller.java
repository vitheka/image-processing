package br.com.vitheka.image_processing.controller;

import br.com.vitheka.image_processing.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buckets")
public class S3Controller {

    private final Logger log = LoggerFactory.getLogger(S3Controller.class);

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/{bucketName}")
    @ResponseStatus(HttpStatus.OK)
    public void createBucket(@PathVariable String bucketName) {
        s3Service.createBucket(bucketName);
    }
}
