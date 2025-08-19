package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BirthDateTest {

    @Test
    void shouldGenerateWithValue(){
        LocalDate dataValida = LocalDate.of(1990,10,9);
        BirthDate birthDate = new BirthDate(dataValida);
        Assertions.assertThat(birthDate.value()).isEqualTo(dataValida);
    }

    @Test
    void shouldNotBeNull(){
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(()-> new BirthDate(null));
    }

    @Test
    void shouldNotBeAfterToday(){
        LocalDate dataFutura = LocalDate.now().plusDays(1);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(dataFutura));
    }

    @Test
    void shouldCorrectlyCalculateAgeInYears(){
        LocalDate dataNascimento = LocalDate.now().minusYears(54);
        BirthDate birthDate = new BirthDate(dataNascimento);
        Assertions.assertThat(birthDate.age().equals(54));
    }


}
