package com.algaworks.algashop.ordering.domain.valueobject;

import java.io.Serializable;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_QUANTITY_IS_NEGATIVE;
import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_QUANTITY_IS_NULL;

public record Quantity(Integer value) implements Serializable, Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity {
        Objects.requireNonNull(value, VALIDATION_ERROR_QUANTITY_IS_NULL);
        if (value < 0) {
            throw new IllegalArgumentException(VALIDATION_ERROR_QUANTITY_IS_NEGATIVE);
        }
    }

    public Quantity add(Quantity quantity) {
        Objects.requireNonNull(quantity, VALIDATION_ERROR_QUANTITY_IS_NULL);
        return new Quantity(this.value + quantity.value());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Quantity o) {
        return this.value.compareTo(o.value);
    }
}
