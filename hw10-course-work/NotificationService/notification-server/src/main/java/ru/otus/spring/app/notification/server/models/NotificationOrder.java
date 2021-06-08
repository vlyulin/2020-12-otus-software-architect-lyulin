package ru.otus.spring.app.notification.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification_orders")
public class NotificationOrder {
    @Id
//    @SequenceGenerator(name="notification_orders_gen", sequenceName = "notification_orders_seq", allocationSize=1, initialValue = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_orders_gen")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name="OBJECT_VERSION_NUMBER")
    private int objectVersionNumber;
    @Column(name="IDEMPOTENCY_KEY")
    private UUID idempotencyKey;
    @Column(name="created_by")
    private Long createdBy;
    @Column(name="SHIP_DATE")
    private Date shipDate;
    @Column(name="STATUS")
    private NotificationOrderSatus status;
    @Column(name="COMPLETE")
    private boolean complete;
    @Column(name="CURRENCY")
    private String currency;
    @OneToMany(fetch=FetchType.EAGER, mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("order")
    private List<NotificationCommodity> items;

    public void setItems(List<NotificationCommodity> items) {
        if(this.items == null) {
            this.items = items;
        }
        else {
            this.items.clear();
            if (items != null) {
                this.items.addAll(items);
            }
        }
    }
};
