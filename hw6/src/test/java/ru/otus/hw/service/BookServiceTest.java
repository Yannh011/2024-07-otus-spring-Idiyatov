package ru.otus.hw.service;

import org.assertj.core.api.Assertions;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.services.BookServiceImpl;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import({BookServiceImpl.class, JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class})
public class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnAllBooks() {
        var actualBooks = bookService.findAll();
        Assertions.assertThat(actualBooks).isNotEmpty()
                .hasSize(3)
                .hasOnlyElementsOfType(Book.class);
        Assertions.assertThat(actualBooks).extracting("title").containsExactly("BookTitle_1", "BookTitle_2", "BookTitle_3");
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnBookById() {

        Long bookId = 1L;
        Optional<Book> actualBook = bookService.findById(bookId);

        assertThat(actualBook).isPresent();

        Book book = actualBook.get();
        assertThat(book)
                .hasFieldOrPropertyWithValue("title", "BookTitle_1");
    }

    @DisplayName("должен сохранять книгу")
    @Test
    void shouldInsertBook() {
        String title = "New Book Title";
        long authorId = 1L;
        long genreId = 2L;

        Book insertedBook = bookService.insert(title, authorId, genreId);
        assertNotNull(insertedBook);
        assertThat(insertedBook.getTitle()).isEqualTo(title);
        assertThat(insertedBook.getAuthor().getId()).isEqualTo(authorId);
        assertThat(insertedBook.getGenre().getId()).isEqualTo(genreId);

        var allBooks = bookService.findAll();
        Assertions.assertThat(allBooks).hasSize(4);
    }

    @DisplayName("Должен изменять книгу")
    @Test
    void shouldUpdateBook(){

        Long bookId = 1L;
        String updatedTitle = "Updated Book Title";
        long updatedAuthorId = 1L;
        long updatedGenreId = 2L;

        Optional<Book> optionalBook = bookService.findById(bookId);
        assertThat(optionalBook).isPresent();

        Book updatedBook = bookService.update(bookId, updatedTitle, updatedAuthorId, updatedGenreId);

        assertNotNull(updatedBook);

        assertThat(updatedBook.getId()).isEqualTo(bookId);
        assertThat(updatedBook.getTitle()).isEqualTo(updatedTitle);
        assertThat(updatedBook.getAuthor().getId()).isEqualTo(updatedAuthorId);
        assertThat(updatedBook.getGenre().getId()).isEqualTo(updatedGenreId);
    }

    @DisplayName("Должен удалять книгу")
    @Test
    void shouldDeleteBook(){
        Long bookIdToDelete = 1L;

        Optional<Book> existingBook = bookService.findById(bookIdToDelete);
        assertThat(existingBook).isPresent();

        bookService.deleteById(bookIdToDelete);

        Optional<Book> deletedBook = bookService.findById(bookIdToDelete);
        assertThat(deletedBook).isNotPresent();

        var remainingBooks = bookService.findAll();
        Assertions.assertThat(remainingBooks).hasSize(2);
    }
}
