package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.GenreController;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnCorrectGenresList() throws Exception {
        List<GenreDTO> genres = List.of(new GenreDTO(1L, "Test Genre"));
        when(genreService.findAll()).thenReturn(genres);
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genres)));
    }
}