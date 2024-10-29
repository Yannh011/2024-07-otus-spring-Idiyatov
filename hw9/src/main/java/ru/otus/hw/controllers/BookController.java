package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.dto.RequestBookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashSet;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listBooks(Model model) {
        List<BookDTO> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") long id, Model model) {
        RequestBookDTO requestBookDTO = bookService.findBookById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", requestBookDTO);
        List<AuthorDTO> authorDTOs = authorService.findAll();
        model.addAttribute("authors", authorDTOs);

        List<GenreDTO> genreDTOs = genreService.findAll();
        model.addAttribute("genres", genreDTOs);
        return "edit";
    }

    @PostMapping("/edit")
    public String saveBook(@ModelAttribute("book") RequestBookDTO book) {
        bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), new HashSet<>(book.getGenreIds()));
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/create")
    public String newBook(Model model) {
        List<AuthorDTO> authorDTOs = authorService.findAll();
        model.addAttribute("authors", authorDTOs);
        List<GenreDTO> genreDTOs = genreService.findAll();
        model.addAttribute("genres", genreDTOs);
        return "create";
    }

    @PostMapping("/create")
    public String saveNewBook(@ModelAttribute("book") RequestBookDTO book) {
        bookService.insert(book.getTitle(), book.getAuthorId(), new HashSet<>(book.getGenreIds()));
        return "redirect:/";
    }
}
