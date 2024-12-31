package br.com.vitheka.image_processing.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandlerAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandlerAdvice.class);


    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<DefaultErrorMessage> handleImageUploadException(ImageUploadException e) {
        log.error("Erro ao enviar imagem: {}", e.getMessage());
        var errorResponse = new DefaultErrorMessage(e.getStatusCode().value(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(ConnectProblemException.class)
    public ResponseEntity<DefaultErrorMessage> handleConnectProblemException(ConnectProblemException e) {
        log.error("Erro ao tentar realizar conexão: {}", e.getMessage());
        var errorResponse = new DefaultErrorMessage(e.getStatusCode().value(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }
}
