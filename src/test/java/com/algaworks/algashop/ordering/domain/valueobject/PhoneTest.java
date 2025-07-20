package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Test
    void shouldGenerateWithValue(){
        Phone phone = new Phone("478-256-2504");
        Assertions.assertThat(phone.phone()).isEqualTo("478-256-2504");
    }

    @Test
    void shouldNotBeNull(){
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Phone(null));
    }
}