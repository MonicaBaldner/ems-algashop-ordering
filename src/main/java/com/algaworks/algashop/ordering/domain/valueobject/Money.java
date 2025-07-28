package com.algaworks.algashop.ordering.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_MONEY_IS_NEGATIVE;
import static com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal value) {
        Objects.requireNonNull(value, VALIDATION_ERROR_MONEY_IS_NULL);
        this.value = value.setScale(2, roundingMode);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(VALIDATION_ERROR_MONEY_IS_NEGATIVE);
        }
    }

    public Money(String value) {
        this(new BigDecimal(Objects.requireNonNull(value)));
    }

    @Override
    public int compareTo(Money other) {
        return this.value.compareTo(other.value);
    }

    public String formatted(Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(value);
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(VALIDATION_ERROR_MONEY_IS_NEGATIVE);
        }
        return new Money(result);
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        BigDecimal multiplied = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(multiplied);
    }

    public Money divide(Money o) {
        return new Money(this.value.divide(o.value, roundingMode));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return value.compareTo(money.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }
}
