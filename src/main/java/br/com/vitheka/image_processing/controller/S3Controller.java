package br.com.vitheka.image_processing.controller;

import br.com.vitheka.image_processing.domain.S3BucketObjectRepresentation;
import br.com.vitheka.image_processing.response.PutImageResponse;
import br.com.vitheka.image_processing.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/buckets")
public class S3Controller {

    private final Logger log = LoggerFactory.getLogger(S3Controller.class);

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createBucket(@RequestBody String bucketName) {
        s3Service.createBucket(bucketName);
    }

    @PostMapping("/{bucketName}/objects")
    @ResponseStatus(HttpStatus.OK)
    public void putObject(@PathVariable String bucketName, @RequestBody S3BucketObjectRepresentation s3BucketObjectRepresentation) {
        s3Service.putObject(bucketName, s3BucketObjectRepresentation);
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public PutImageResponse uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("bucketName") String bucketName) throws IOException {

        var objectName = Objects.requireNonNull(file.getOriginalFilename(), "O nome do arquivo não pode ser nulo!");

        byte[] imageBytes = file.getBytes();

        s3Service.putImage(bucketName, objectName, imageBytes);

        return new PutImageResponse("Imagem enviada com sucesso: ".concat(objectName));
    }
}
