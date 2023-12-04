package model;

// Java Bean -

// POJO - Plain Old Java Object


import java.math.BigDecimal;
import java.time.LocalDate;

public class Book{

    private Long id;

    private String author;

    private String title;

    private LocalDate publishedDate;

    private BigDecimal price;
    private int quantity;
    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setEmployeeId(Long employeeId){
        this.employeeId = employeeId;
    }
    public Long getEmployeeId(){
        return this.employeeId;
    }
    @Override
    public String toString(){
        return String.format("Id: %d | EmployeeId: %s | Title: %s | Author: %s | Date: %s", this.id, this.employeeId, this.title, this.author, this.publishedDate);
    }
}