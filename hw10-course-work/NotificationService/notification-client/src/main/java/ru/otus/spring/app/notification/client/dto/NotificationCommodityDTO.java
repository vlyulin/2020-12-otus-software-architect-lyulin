package ru.otus.spring.app.notification.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotificationCommodityDTO {
    private long id;
    private int quantity;
    private float price;
}
