package ru.otus.spring.app.order.server.models;

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
@Table(name = "orders")
public class Order {
    @Id
    @SequenceGenerator(name="orders_gen", sequenceName = "orders_seq", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_gen")
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
    private OrderSatus status;
    @Column(name="COMPLETE")
    private boolean complete;
    @OneToMany(fetch=FetchType.EAGER, mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("order")
    private List<Commodity> items;

    public void setItems(List<Commodity> items) {
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
