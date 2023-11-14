package model;

import java.time.LocalDate;

// POJO - Plain Old Java Object
//Java Bean
public interface Book{
    public Long getId();

    public void setId(Long id);

    public String getAuthor();

    public void setAuthor(String author);

    public String getTitle();

    public void setTitle(String title);

    public LocalDate getPublishedDate();

    public void setPublishedDate(LocalDate publishedDate);
    public String toString();

}