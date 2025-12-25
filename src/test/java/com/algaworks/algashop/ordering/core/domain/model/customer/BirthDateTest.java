package com.algaworks.algashop.ordering.core.domain.model.customer;

import com.algaworks.algashop.ordering.core.domain.model.ErrorMessages;
import com.algaworks.algashop.ordering.core.domain.model.customer.BirthDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

class BirthDateTest {

    @Test
    void shouldGenerateWithBirthDateValue() {
        BirthDate birthDate = new BirthDate(LocalDate.of(1970,10,9));
        Assertions.assertThat(birthDate.value()).isEqualTo("1970-10-09");
    }

    @Test
    void given_nullBirthDate_whenTryToInstantiate_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> BirthDate.of(null));
    }

    @Test
    void given_futureBirthDate_whenTryToInstantiate_shouldGenerateException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> BirthDate.of(futureDate))
                .withMessage(ErrorMessages.VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
    }

    @Test
    void given_validBirthDate_whenCallAge_shouldReturnAgeInYears() {
        LocalDate birthDateValue = LocalDate.of(1970, 10, 9);
        BirthDate birthDate = BirthDate.of(birthDateValue);

        int expectedAge = Period.between(birthDateValue, LocalDate.now()).getYears();

        Assertions.assertThat(birthDate.age()).isEqualTo(expectedAge);
    }

    @Test
    void given_validBirthDate_whenCallToString_shouldReturnStringDate() {
        LocalDate value = LocalDate.of(1990, 1, 1);
        BirthDate birthDate = BirthDate.of(value);
        String birthDateString = value.toString();

        Assertions.assertThat(birthDateString).isEqualTo(birthDate.toString());
    }

}