package model.builder;

import model.Order;

import java.time.LocalDate;

public class OrderBuilder {

    private Order order;

    public OrderBuilder() {
        this.order = new Order();
    }

    public OrderBuilder setId(Long id) {
        order.setId(id);
        return this;
    }

    public OrderBuilder setCustomerId(Long customerId) {
        order.setCustomerId(customerId);
        return this;
    }

    public OrderBuilder setBookId(Long bookId) {
        order.setBookId(bookId);
        return this;
    }

    public OrderBuilder setPurchaseDate(LocalDate purchaseDate) {
        order.setPurchaseDate(purchaseDate);
        return this;
    }

    public Order build() {
        return order;
    }
}
