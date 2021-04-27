package ru.otus.spring.app.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "commodities")
public class Commodity {
    @Id
    @SequenceGenerator(name="commodities_gen", sequenceName = "commodities_seq", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commodities_gen")
    @Column(name = "id", updatable = true, nullable = true)
    private Long id;
    @Column(name="quantity")
    private int quantity;
    @Column(name="price")
    private float price;
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="order_id", updatable = true, insertable = true)
    @JsonIgnoreProperties("items")
    Order order;
}
