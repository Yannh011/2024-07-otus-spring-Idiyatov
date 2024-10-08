package ru.otus.hw.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями должен ")
@DataJpaTest
class JpaCommentRepositoryTest {

    private static final int COMMENTS_COUNT_BY_FIRST_ID_BOOK = 2;

    private static final long FIRST_COMMENT_ID = 1L;
    private static final long FIRST_BOOK_ID = 1L;
    private static final long NON_EXISTENT_COMMENT_ID = 999L;
    private static final String FIRST_COMMENT_TEXT = "Comment_1_1";

    @Autowired
    private CommentRepository jpaCommentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Book bookById1;

    @BeforeEach
    void setUp() {
        bookById1 = new Book(FIRST_BOOK_ID, "BookTitle_1",
                new Author(1L, "Author_1"),
                new Genre(1L, "Genre_1"));
    }

    @DisplayName("возвращать все комменты по id книги")
    @Test
    void findAllByBookId() {
        List<Comment> comments = jpaCommentRepository.findAllByBookId(FIRST_BOOK_ID);
        assertThat(comments).hasSize(COMMENTS_COUNT_BY_FIRST_ID_BOOK);
    }

    @DisplayName("возвращать коммент по его id")
    @Test
    void findById() {
        Optional<Comment> comment = jpaCommentRepository.findById(FIRST_COMMENT_ID);
        assertThat(comment).isNotEmpty().get()
                .hasFieldOrPropertyWithValue("text", FIRST_COMMENT_TEXT);
    }

    @DisplayName("возвращать Optional.empty когда коммент по id не найден")
    @Test
    void findByIdNotFound() {
        Optional<Comment> comment = jpaCommentRepository.findById(NON_EXISTENT_COMMENT_ID);
        assertThat(comment).isEmpty();
    }

    @DisplayName("сохранять коммент в бд")
    @Test
    void saveNewComment() {
        Comment newComment = new Comment(0, "text new comment", bookById1);
        Comment savedComment = jpaCommentRepository.save(newComment);
        List<Comment> comments = jpaCommentRepository.findAllByBookId(FIRST_BOOK_ID);

        assertThat(savedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .isEqualTo(newComment);
        assertThat(comments.size()).isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK + 1);
    }

    @DisplayName("обновляет коммент в бд")
    @Test
    void updateComment() {
        Comment commentBeforeUpdate = jpaCommentRepository.findById(FIRST_COMMENT_ID).get();

        entityManager.detach(commentBeforeUpdate);

        Comment commentToUpdate = new Comment(FIRST_COMMENT_ID, "Updated comment", bookById1);
        Comment updatedComment = jpaCommentRepository.save(commentToUpdate);
        List<Comment> comments = jpaCommentRepository.findAllByBookId(FIRST_BOOK_ID);

        Comment commentAfterUpdate = jpaCommentRepository.findById(FIRST_COMMENT_ID).get();

        assertThat(updatedComment).isEqualTo(commentToUpdate);
        assertThat(comments.size()).isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK);

        assertThat(commentAfterUpdate).isNotEqualTo(commentBeforeUpdate);
    }

    @DisplayName("удаляет коммент из бд по id")
    @Test
    void deleteById() {
        Optional<Comment> willBeDeletedComment = jpaCommentRepository.findById(FIRST_COMMENT_ID);
        List<Comment> commentsBeforeDeletedOne = jpaCommentRepository.findAllByBookId(FIRST_BOOK_ID);

        jpaCommentRepository.deleteById(FIRST_COMMENT_ID);

        List<Comment> commentsAfterDeletedOne = jpaCommentRepository.findAllByBookId(FIRST_BOOK_ID);
        Optional<Comment> deletedComment = jpaCommentRepository.findById(FIRST_COMMENT_ID);

        assertThat(willBeDeletedComment).isNotEmpty();
        assertThat(commentsBeforeDeletedOne.size()).isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK);

        assertThat(commentsAfterDeletedOne.size()).isEqualTo(COMMENTS_COUNT_BY_FIRST_ID_BOOK - 1);
        assertThat(deletedComment).isEmpty();
    }
}