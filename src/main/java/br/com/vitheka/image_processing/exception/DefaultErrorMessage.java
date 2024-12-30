package br.com.vitheka.image_processing.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DefaultErrorMessage(int status, String message) {
}
