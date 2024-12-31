package br.com.vitheka.image_processing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConnectProblemException extends ResponseStatusException {

    public ConnectProblemException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
}
