package com.algaworks.algashop.ordering.domain.model.commons;

import com.algaworks.algashop.ordering.domain.model.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.FieldValidations;

public record Email(String value) {

    public Email(String value) {
        FieldValidations.requiresValidEmail(value, ErrorMessages.VALIDATION_ERROR_EMAIL_IS_INVALID);

        this.value = value.trim();
    }

    public static Email of(String value) {
        return new Email(value);
    }

    @Override
    public String toString() {
        return value;
    }
}