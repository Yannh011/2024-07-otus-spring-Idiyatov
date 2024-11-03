package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.CommentController;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    public static final String ERROR = "Entity not found!";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    private CommentDTO commentDTO;

    @BeforeEach
    void getCommentDTO() {
        commentDTO = new CommentDTO(1L, "Text", 1L);
    }

    @Test
    void shouldReturnCorrectCommentsListByBookId() throws Exception {
        List<CommentDTO> comments = List.of(commentDTO);
        when(commentService.findAllByBookId(1L)).thenReturn(comments);
        mockMvc.perform(get("/api/comments").param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comments)));
    }

    @Test
    void shouldReturnCorrectCommentById() throws Exception {
        when(commentService.findById(1L)).thenReturn(Optional.of(commentDTO));
        mockMvc.perform(get("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDTO)));
    }

    @Test
    void shouldUpdateComment() throws Exception {
        CommentDTO updatedComment = new CommentDTO(1L,"New Text", 2L);
        mockMvc.perform(put("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedComment)))
                .andExpect(status().isOk());
        verify(commentService).update(eq(1L), eq("New Text"), eq(2L));
    }

    @Test
    void shouldDeleteCommentById() throws Exception {
        doNothing().when(commentService).deleteById(1L);
        mockMvc.perform(delete("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(commentService).deleteById(1);
    }

    @Test
    void shouldInsertNewComment() throws Exception {
        CommentDTO newComment = new CommentDTO(0, "New Text", 2L);
        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newComment)))
                .andExpect(status().isOk());
        verify(commentService).insert(eq("New Text"), eq(2L));
    }

    @Test
    void shouldReturnExpectedErrorWhenCommentNotFound() throws Exception {
        when(commentService.findById(5L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/comments/5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERROR));
    }
}