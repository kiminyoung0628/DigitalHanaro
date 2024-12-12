package org.example.springassignment1.service;

import org.example.springassignment1.domain.Book;
import org.example.springassignment1.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookMapper bookMapper;

    public List<Book> getAllBooks() {
        List<Book> books = bookMapper.getAllBooks();
        // 가져온 데이터 출력
        books.forEach(System.out::println);
        return books;
    }

    public Book getBookById(int bno) {
        return bookMapper.getBookById(bno);
    }

    public boolean borrowBook(int bno, String borrowerId) {
        Book book = bookMapper.getBookById(bno);
        if (book.isAvailability()) {
            bookMapper.updateBorrowInfo(bno, borrowerId);
            return true;
        }
        return false;
    }
}
