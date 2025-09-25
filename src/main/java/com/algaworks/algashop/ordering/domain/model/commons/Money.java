package com.algaworks.algashop.ordering.domain.model.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.VALIDATION_ERROR_MONEY_IS_NEGATIVE;
import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        Objects.requireNonNull(value, VALIDATION_ERROR_MONEY_IS_NULL); //todo mensagem
        this.value = value.setScale(2, roundingMode);
        if (this.value.signum() == -1) {
            throw new IllegalArgumentException();//todo mensagem
        }
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        BigDecimal multiplied = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(multiplied);
    }

    public String formatted(Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(value);
    }

    public Money add(Money money) {
        Objects.requireNonNull(money);
        return new Money(this.value.add(money.value));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(Money o) {
        return this.value.compareTo(o.value);
    }

    public Money divide(Money o) {
        return new Money(this.value.divide(o.value, roundingMode));
    }

    public Money subtract(Money other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(VALIDATION_ERROR_MONEY_IS_NEGATIVE);
        }
        return new Money(result);
    }

}