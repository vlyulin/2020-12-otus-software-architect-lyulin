package ru.otus.spring.app.billing.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.app.billing.client.dto.CurrenciesEnum;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "billing_orders")
public class BillingOrder {
    @Id
    @SequenceGenerator(name="billing_order_gen", sequenceName = "billing_order_seq", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_order_gen")
    @Column(name = "id", updatable = false, nullable = false)
    private int billingOrderId;
    @Column(name="account_id")
    private int accountId;
    @Column(name="amount")
    private float amount;
    @Column(name="currency")
    private CurrenciesEnum currency;
}
