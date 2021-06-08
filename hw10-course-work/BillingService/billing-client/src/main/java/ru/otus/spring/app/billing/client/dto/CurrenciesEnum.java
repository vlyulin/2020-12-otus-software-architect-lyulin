package ru.otus.spring.app.billing.client.dto;

public enum CurrenciesEnum {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private String currency;

    CurrenciesEnum(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return this.currency;
    }
}
