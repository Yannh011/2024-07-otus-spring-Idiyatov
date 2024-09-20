package ru.otus.hw.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.JpaAuthorRepository;

import java.util.List;
import java.util.stream.IntStream;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import(JpaAuthorRepository.class)
public class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository repositoryJpa;

        @DisplayName("должен загружать список всех авторов")
        @Test
        void shouldReturnCorrectAuthorsList() {
            var expectedAuthors = getDbAuthors();
            var actualAuthors = repositoryJpa.findAll();
            Assertions.assertThat(actualAuthors)
                    .isNotNull()
                    .isNotEmpty()
                    .isEqualTo(expectedAuthors);
        }

        @DisplayName("должен загружать автора по id")
        @Test
        void shouldReturnCorrectAuthorById() {
            var expectedAuthor = new Author(2, "Author_2");
            var actualAuthor = repositoryJpa.findById(2);
            Assertions.assertThat(actualAuthor)
                    .isPresent()
                    .get()
                    .isEqualTo(expectedAuthor);
        }

        private static List<Author> getDbAuthors() {
            return IntStream.range(1, 4).boxed()
                    .map(id -> new Author(id, "Author_" + id))
                    .toList();
        }

}




