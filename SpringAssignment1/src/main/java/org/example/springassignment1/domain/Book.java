package org.example.springassignment1.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class Book {
    private int bno;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String isbn;
    private boolean availability;
    private String borrowerId;
    private Date startDate;
    private Date endDate;

    @Override
    public String toString() {
        return "Book{" +
                "bno=" + bno +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                ", availability=" + availability +
                ", borrowerId='" + borrowerId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
