package com.btg.btg_funds.service.vo;

import com.btg.btg_funds.exception.BusinessException;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private final BigDecimal value;

    private Money(BigDecimal value) {
        if (value == null) {
            throw new BusinessException("El monto no puede ser nulo");
        }
        this.value = value;
    }

    public static Money of(Double value) {
        if (value == null) {
            throw new BusinessException("El monto no puede ser nulo");
        }
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money positive(Double value) {
        Money money = of(value);
        if (money.isZeroOrNegative()) {
            throw new BusinessException("El monto debe ser mayor a cero");
        }
        return money;
    }

    public boolean isLessThan(Money other) {
        return this.value.compareTo(other.value) < 0;
    }

    public boolean isNegative() {
        return this.value.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isZeroOrNegative() {
        return this.value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public void validateNotLessThan(Money other, String message) {
        if (this.isLessThan(other)) {
            throw new BusinessException(message);
        }
    }

    public Double toDouble() {
        return value.doubleValue();
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return value.compareTo(money.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
