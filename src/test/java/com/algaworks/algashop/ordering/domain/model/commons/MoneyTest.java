package com.algaworks.algashop.ordering.domain.model.commons;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldGenerateWithValue() {
        Money money = new Money(new BigDecimal("150.50"));
        Assertions.assertThat(money.value()).isEqualTo(new BigDecimal("150.50"));
    }


    @Test
    void shouldAddValue() {
        Money money = new Money("150.50");
        Assertions.assertThat(money.add(new Money(BigDecimal.TEN))).isEqualTo(new Money("160.50"));
    }

    @Test
    void shouldSubtractValue(){
        Money money = new Money("150.50");
        Assertions.assertThat(money.subtract(new Money("150.50"))).isEqualTo(Money.ZERO);
    }

    @Test
    void shouldNotSubtractValue() {
        Money money = new Money("150.50");
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->money.subtract(new Money("150.51")));
    }

    @Test
    void shoulMultiplyValue() {
        Money money = new Money("150.50");
        Assertions.assertThat(money.multiply(new Quantity(2))).isEqualTo(new Money("301"));
    }

    @Test
    void shouldNotMultiplyValue(){
        Money money = new Money("150.50");
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> money.multiply( Quantity.ZERO));
    }

    @Test
    void shouldDivideValue() {
        Money money = new Money("150.50");
        Assertions.assertThat(money.divide(new Money("10.11"))).isEqualTo(new Money("14.89"));
    }


    @Test
    void shouldFormatMoneyBasedOnLocale() {
        Money money = new Money("150.50");
        String formattedBR = money.formatted(new Locale("pt", "BR"));
        String formattedUS = money.formatted(Locale.US);

        assertTrue(formattedBR.contains("R$"));
        assertTrue(formattedUS.contains("$"));
    }

    @Test
    void shouldRejectNullValue() {
        assertThrows(NullPointerException.class, () -> new Money((BigDecimal) null));
        assertThrows(NullPointerException.class, () -> new Money((String) null));
    }

    @Test
    void shouldRejectNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new Money("-0.01"));
    }

    @Test
    void shouldCheckEqualityRegardlessOfScale() {
        Money one = new Money("1.0");
        Money two = new Money("1.00");
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
    }

    @Test
    void shouldUseZeroConstantProperly() {
        Money zero1 = new Money("0.00");
        Money zero2 = Money.ZERO;
        assertEquals(zero1, zero2);
        assertEquals(0, zero2.value().compareTo(BigDecimal.ZERO));
    }

}