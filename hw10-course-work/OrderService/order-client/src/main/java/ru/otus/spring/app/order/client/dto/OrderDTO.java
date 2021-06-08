package ru.otus.spring.app.order.client.dto;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    private long id;
    private int objectVersionNumber;
    private long createdBy;
    private Date shipDate;
    private boolean complete;
    private List<CommodityDTO> items;
}
