package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldGenerateWithValue(){
        Email email = new Email("teste@email.com");
        Assertions.assertThat(email.email()).isEqualTo("teste@email.com");
    }

    @Test
    void shouldNotBeNull(){
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(()-> new Email(null));
    }

    @Test
    void shouldBeAValidEmail(){
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Email("emailinvalidosemarroba.com"))
                .withMessage("e-mail inválido!!!");
    }
}