package com.algaworks.algashop.ordering.domain.valueobject;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.*;
import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_ADDRESS_IS_NULL;

@Builder(toBuilder = true)
public record Shipping(Money cost, LocalDate expectedDate, Recipient recipient, Address address) {

    public Shipping {
        Objects.requireNonNull(recipient);
        Objects.requireNonNull(address, VALIDATION_ERROR_ADDRESS_IS_NULL);
        Objects.requireNonNull(cost);
        Objects.requireNonNull(expectedDate);
    }

}
