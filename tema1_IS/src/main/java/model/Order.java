package model;

import java.time.LocalDate;

public class Order {

    private Long id;
    private Long customerId;
    private Long bookId;
    private LocalDate purchaseDate;

    public Order() {
    }

    public Order(Long id, Long customerId, Long bookId, LocalDate purchaseDate) {
        this.id = id;
        this.customerId = customerId;
        this.bookId = bookId;
        this.purchaseDate = purchaseDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
