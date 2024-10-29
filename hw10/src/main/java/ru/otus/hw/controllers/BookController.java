package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.RequestBookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long id) {
        BookDTO book = bookService.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found!"));
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<Void> insertBook(@RequestBody RequestBookDTO book) {
        bookService.insert(book.getTitle(), book.getAuthorId(), new HashSet<>(book.getGenreIds()));
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBook(@RequestBody RequestBookDTO book) {
        bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), new HashSet<>(book.getGenreIds()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body("Entity not found!");
    }
}