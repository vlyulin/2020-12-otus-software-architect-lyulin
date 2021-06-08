package ru.otus.spring.app.notification.client.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotificationOrderDTO {
    private long id;
    private int objectVersionNumber;
    private UUID idempotencyKey;
    private long createdBy;
    private Date shipDate;
    private boolean complete;
    private NotificationCurrenciesEnum currency;
    private List<NotificationCommodityDTO> items;
}
