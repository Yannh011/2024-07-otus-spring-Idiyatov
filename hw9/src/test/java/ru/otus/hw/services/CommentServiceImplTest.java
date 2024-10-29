package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.services.mappers.AuthorMapperImpl;
import ru.otus.hw.services.mappers.BookMapperImpl;
import ru.otus.hw.services.mappers.CommentMapperImpl;
import ru.otus.hw.services.mappers.GenreMapperImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentServiceImpl.class,
        CommentMapperImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    private static List<CommentDTO> commentDTOs;

    @BeforeAll
    public static void initCommentDTOs() {
        commentDTOs = List.of(
                new CommentDTO(1L, "text_1", 1L),
                new CommentDTO(2L, "text_2", 2L),
                new CommentDTO(3L, "text_3", 1L)
        );
    }

    @Test
    void shouldReturnCorrectCommentById() {
        Optional<CommentDTO> actualCommentDTO = commentService.findById(1L);
        assertThat(actualCommentDTO).isPresent().get().isEqualTo(commentDTOs.get(0));
    }

    @Test
    void whenAllCommentsByBooksIdFound() {
        List<CommentDTO> actualCommentDTOs = commentService.findAllByBookId(1L);
        assertThat(actualCommentDTOs).isEqualTo(List.of(commentDTOs.get(0), commentDTOs.get(2)));
    }

    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        CommentDTO actualCommentDTO = commentService.insert("new_text", 1L);
        CommentDTO expectedCommentDTO = new CommentDTO(4L, "new_text", 1L);
        assertThat(actualCommentDTO).isEqualTo(expectedCommentDTO);
    }

    @Test
    @DirtiesContext
    void shouldUpdateComment() {
        CommentDTO actualCommentDTO = commentService.update(1L, "new_text", 1L);
        CommentDTO expectedCommentDTO = new CommentDTO(1L, "new_text", 1L);
        assertThat(actualCommentDTO).isEqualTo(expectedCommentDTO);
    }

    @Test
    @DirtiesContext
    void shouldDeleteComment() {
        commentService.deleteById(1L);
        List<CommentDTO> actualCommentDTOs = commentService.findAllByBookId(1L);
        assertThat(actualCommentDTOs).isEqualTo(List.of(commentDTOs.get(2)));
    }

}