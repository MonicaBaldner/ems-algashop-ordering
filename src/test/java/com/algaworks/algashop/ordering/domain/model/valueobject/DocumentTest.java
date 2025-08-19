package com.algaworks.algashop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void shouldGenerateWithValue(){
        Document document = new Document("255-08-0578");
        Assertions.assertThat(document.document()).isEqualTo("255-08-0578");
    }

    @Test
    void shouldNotBeNull(){
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Document(null));
    }



}