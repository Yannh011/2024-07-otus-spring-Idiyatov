package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsListByBookId() {
        var actualComments = repositoryJpa.findByBookId(1L);
        var expectedComments = List.of(
                em.find(Comment.class, 1L),
                em.find(Comment.class, 3L)
        );
        assertThat(actualComments).isEqualTo(expectedComments);
    }

}