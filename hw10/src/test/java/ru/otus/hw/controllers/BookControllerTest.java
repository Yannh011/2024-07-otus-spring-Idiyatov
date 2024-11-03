package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.BookController;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.RequestBookDTO;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    public static final String ERROR = "Entity not found!";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    private BookDTO bookDTO;

    @BeforeEach
    void getBookDTO() {
        bookDTO = new BookDTO(1L, "Test Book", new AuthorDTO(1L, "Author Name"), new ArrayList<>());
    }

    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        List<BookDTO> books = List.of(bookDTO);
        when(bookService.findAll()).thenReturn(books);
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books)));
    }

    @Test
    void shouldReturnCorrectBookById() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(bookDTO));
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        RequestBookDTO requestBook = new RequestBookDTO(1L,"New Title", 2L, List.of(1L));
        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestBook)))
                .andExpect(status().isOk());
        verify(bookService).update(eq(1L), eq("New Title"), eq(2L), anySet());
    }

    @Test
    void shouldDeleteBookById() throws Exception {
        doNothing().when(bookService).deleteById(1L);
        mockMvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookService).deleteById(1);
    }

    @Test
    void shouldInsertNewBook() throws Exception {
        RequestBookDTO requestBook = new RequestBookDTO(0, "New Book", 2L, List.of(2L));
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestBook)))
                .andExpect(status().isOk());
        verify(bookService).insert(eq("New Book"), eq(2L), anySet());
    }

    @Test
    void shouldReturnExpectedErrorWhenBookNotFound() throws Exception {
        when(bookService.findById(5L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/books/5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR));
    }
}