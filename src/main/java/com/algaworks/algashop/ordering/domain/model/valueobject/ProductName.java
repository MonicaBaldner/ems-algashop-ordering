package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.FieldValidations;

import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_PRODUCTNAME_IS_BLANK;

public record ProductName(String value) {

    public ProductName {

        FieldValidations.requiresNonBlank(value,VALIDATION_ERROR_PRODUCTNAME_IS_BLANK);
    }

    @Override
    public String toString() {
        return  value;
    }
}
