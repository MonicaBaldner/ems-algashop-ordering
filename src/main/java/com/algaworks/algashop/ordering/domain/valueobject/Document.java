package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record Document(String document) {

    public Document(String document) {
        Objects.requireNonNull(document);

        if(document.isBlank()){
            throw new IllegalArgumentException();
        }

        this.document = document.trim();
    }

    @Override
    public String toString() {
        return document.toString();
    }
}
