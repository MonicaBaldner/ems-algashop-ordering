package com.algaworks.algashop.ordering.domain.model.valueobject;

import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.VALIDATION_ERROR_ZIPCODE_MUST_HAVE_FIVE_CHARACTERS;

public record ZipCode(String value) {

    public ZipCode {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException(VALIDATION_ERROR_ZIPCODE_MUST_HAVE_FIVE_CHARACTERS);
        }
        if (value.length() != 5) {
            throw new IllegalArgumentException(VALIDATION_ERROR_ZIPCODE_MUST_HAVE_FIVE_CHARACTERS);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
