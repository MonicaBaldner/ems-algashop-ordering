package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    @Test
    void shouldGenerateWithValue(){
        FullName fullName = new FullName("Zé", "Mané");
        Assertions.assertThat(fullName.firstName()).isEqualTo("Zé");
        Assertions.assertThat(fullName.lastName()).isEqualTo("Mané");
    }

    @Test
    void shouldNotBeNull(){
        FullName fullName = new FullName("Zé", "Mané");
        Assertions.assertThat(fullName).extracting("firstName", "lastName")
                .doesNotContainNull();
    }

    @Test
    void shouldNotBeNullWithMessages(){

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName(null, "Mané"))
                .withMessage("First name must not be null.");

        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new FullName("Zé", null))
                .withMessage("Last name must not be null.");

    }

}