package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DisplayName("Сервис для работы с комментариями ")
@DataJpaTest
@Import({CommentServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    private List<Book> dbBooks;

    private static List<CommentDto> commentDTOs;

    @BeforeEach
    void setUp() {
        List<Author> dbAuthors = getDbAuthors();
        List<Genre> dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        commentDTOs = List.of(
                new CommentDto(1L, "CommentText_1"),
                new CommentDto(2L, "CommentText_2"),
                new CommentDto(3L, "CommentText_3")
        );
    }

    @DisplayName("должен возвращать комментарий по id")
    @ParameterizedTest
    @MethodSource("getCommentsDto")
    void shouldReturnCorrectCommentById(CommentDto expectedCommentDto) {
        var actualCommentDto = commentService.findById(expectedCommentDto.getId());

        assertThat(actualCommentDto).isPresent()
                .get()
                .isEqualTo(expectedCommentDto);
    }

    @Test
    void shouldReturnCorrectCommentById() {
        Optional<CommentDto> actualCommentDTO = commentService.findById(1L);
        assertThat(actualCommentDTO).isPresent().get().isEqualTo(commentDTOs.get(0));
    }

    @Test
    @DirtiesContext
    void shouldSaveNewComment() {
        var actualCommentDTO = commentService.insert("new_text", 1L);
        var expectedCommentDTO = new CommentDto(7L, "new_text");
        assertThat(actualCommentDTO).isEqualTo(expectedCommentDTO);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен сохранять изменённый комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var actualCommentDto = commentService.update(1L, "CommentText_1000", dbBooks.get(2).getId());
        var expectedCommentDto = new CommentDto(1L, "CommentText_1000");

        assertThat(actualCommentDto).isEqualTo(expectedCommentDto);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(commentService.findById(1L)).isNotEmpty();

        assertThatCode(() -> commentService.deleteById(1L)).doesNotThrowAnyException();

        assertThat(commentService.findById(1L)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<CommentDto> getCommentsDto(List<Book> dbBooks) {
        return IntStream.range(1, 3).boxed()
                .map(id -> new Comment(id,
                        "CommentText_" + id,
                        dbBooks.get((id - 1) / 2)
                ))
                .map(CommentDto::toDto)
                .toList();
    }

    private static List<CommentDto> getCommentsDto() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var dbBooks = getDbBooks(dbAuthors, dbGenres);
        return getCommentsDto(dbBooks);
    }
}
