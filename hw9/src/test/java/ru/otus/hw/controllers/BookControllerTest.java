package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.dto.RequestBookDTO;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO(1L, "Test Book", new AuthorDTO(1L, "Author Name"), new ArrayList<>());
    }

    @Test
    void listBooks() throws Exception {
        List<BookDTO> books = List.of(bookDTO);
        when(bookService.findAll()).thenReturn(books);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void editBook() throws Exception {
        RequestBookDTO requestBookDTO = new RequestBookDTO(1L, "Test Book", 1L, new ArrayList<>());
        when(bookService.findBookById(1L)).thenReturn(Optional.of(requestBookDTO));
        when(authorService.findAll()).thenReturn(List.of(new AuthorDTO(1L, "Author Name")));
        when(genreService.findAll()).thenReturn(List.of(new GenreDTO(1L, "Genre Name")));
        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void saveBook() throws Exception {
        when(bookService.update(anyLong(), anyString(), anyLong(), anySet())).thenReturn(null);
        mockMvc.perform(post("/edit")
                        .param("id", "1")
                        .param("title", "Updated Title")
                        .param("authorId", "1")
                        .param("genreIds", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(bookService).update(eq(1L), eq("Updated Title"), eq(1L), anySet());
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteById(1L);
        mockMvc.perform(post("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(bookService).deleteById(1L);
    }

    @Test
    void newBook() throws Exception {
        when(authorService.findAll()).thenReturn(List.of(new AuthorDTO(1L, "Author Name")));
        when(genreService.findAll()).thenReturn(List.of(new GenreDTO(1L, "Genre Name")));
        mockMvc.perform(get("/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void saveNewBook() throws Exception {
        when(bookService.insert(anyString(), anyLong(), anySet())).thenReturn(bookDTO);
        mockMvc.perform(post("/create")
                        .param("title", "New Book")
                        .param("authorId", "1")
                        .param("genreIds", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(bookService).insert(eq("New Book"), eq(1L), anySet());
    }
}