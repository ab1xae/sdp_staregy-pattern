package com.university.registration.rates;

import java.math.BigDecimal;

public final class FixedExchangeRate implements ExchangeRateSource {

    private final BigDecimal rate;

    public FixedExchangeRate(String rate) {
        this.rate = new BigDecimal(rate);
    }

    @Override
    public BigDecimal kztPerUsd() {
        return rate;
    }
}
