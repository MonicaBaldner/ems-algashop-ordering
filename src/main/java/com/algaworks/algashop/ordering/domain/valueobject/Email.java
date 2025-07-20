package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.FieldValidations;

import java.util.Objects;

public record Email(String email) {

    public Email(String email) {
        Objects.requireNonNull(email);
        FieldValidations.requiresValidEmail(email, "e-mail inválido!!!");

        this.email = email.trim();
    }

    @Override
    public String toString() {
        return  email;
    }


}
