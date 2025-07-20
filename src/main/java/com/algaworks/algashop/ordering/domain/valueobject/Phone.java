package com.algaworks.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record Phone(String phone) {

    public Phone(String phone) {

        Objects.requireNonNull(phone);
        if(phone.isBlank()){
            throw new IllegalArgumentException();
        }

        this.phone = phone.trim();
    }

    @Override
    public String toString() {
        return  phone ;
    }
}
