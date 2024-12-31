package br.com.vitheka.image_processing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ImageUploadException extends ResponseStatusException {

    public ImageUploadException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
}
