package org.example.springassignment1.controller;


import org.example.springassignment1.domain.Book;
import org.example.springassignment1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    // 목록 페이지

    @GetMapping("/test")
    @ResponseBody
    public String testEndpoint() {
        return "BookController is working!";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "book-list";
    }

    // 조회 페이지
    @GetMapping("/read/{bno}")
    public String read(@PathVariable int bno, Model model) {
        model.addAttribute("book", bookService.getBookById(bno));
        return "book-read";
    }

    @PostMapping("/borrow")
    public String borrow(
            @RequestParam int bno,
            @RequestParam String borrowerId,
            Model model
    ) {
        boolean success = bookService.borrowBook(bno, borrowerId);
        Book book = bookService.getBookById(bno); // 대출 후 도서 정보 조회

        // 리다이렉트에 필요한 데이터를 쿼리 파라미터로 전달
        return "redirect:/book/result?bno=" + bno + "&success=" + success + "&borrowerId=" + borrowerId;
    }

    @GetMapping("/result")
    public String result(
            @RequestParam int bno,
            @RequestParam boolean success,
            @RequestParam String borrowerId,
            Model model
    ) {
        Book book = bookService.getBookById(bno);
        model.addAttribute("success", success);
        model.addAttribute("book", book);
        model.addAttribute("borrowerId", borrowerId);
        return "book-result";
    }

}
