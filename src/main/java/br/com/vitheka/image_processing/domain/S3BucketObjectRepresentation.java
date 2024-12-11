package br.com.vitheka.image_processing.domain;

public record S3BucketObjectRepresentation(
        String objectName,
        String text
) {}
