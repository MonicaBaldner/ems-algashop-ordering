package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public record BirthDate(LocalDate value) {

        public BirthDate {
            Objects.requireNonNull(value);
            if (value.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
            }
        }

        public Integer age() {
          //  return (int) Duration.between(value, LocalDate.now()).toDays();
            return Period.between(value, LocalDate.now()).getYears();
        }

        @Override
        public String toString() {
            return value.toString();
        }
}
