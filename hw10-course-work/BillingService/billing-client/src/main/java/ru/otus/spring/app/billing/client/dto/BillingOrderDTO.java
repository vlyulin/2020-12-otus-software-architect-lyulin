package ru.otus.spring.app.billing.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BillingOrderDTO {
    private int accountId;
    private float amount;
    private CurrenciesEnum currency;
}
