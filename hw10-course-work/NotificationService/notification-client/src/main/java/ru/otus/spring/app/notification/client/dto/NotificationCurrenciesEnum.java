package ru.otus.spring.app.notification.client.dto;

public enum NotificationCurrenciesEnum {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private String currency;

    NotificationCurrenciesEnum(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return this.currency;
    }
}
