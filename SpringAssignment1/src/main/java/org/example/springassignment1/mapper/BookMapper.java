package org.example.springassignment1.mapper;

import org.apache.ibatis.annotations.*;
import org.example.springassignment1.domain.Book;

import java.util.List;

@Mapper
public interface BookMapper {
    @Select("SELECT * FROM book")
    List<Book> getAllBooks();

    @Select("SELECT * FROM book WHERE bno = #{bno}")
    Book getBookById(@Param("bno") int bno);

    @Update("UPDATE book SET availability = 0, borrowerId = #{borrowerId}, startDate = NOW(), endDate = DATE_ADD(NOW(), INTERVAL 14 DAY) WHERE bno = #{bno}")
    void updateBorrowInfo(@Param("bno") int bno, @Param("borrowerId") String borrowerId);
}
