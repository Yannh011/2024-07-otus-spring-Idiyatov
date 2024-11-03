package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.mappers.AuthorMapperImpl;
import ru.otus.hw.services.mappers.BookMapperImpl;
import ru.otus.hw.services.mappers.GenreMapperImpl;
import ru.otus.hw.services.mappers.RequestBookMapperImpl;
import ru.otus.hw.services.mappers.RequestGenreMapperImpl;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class,
        RequestBookMapperImpl.class, RequestGenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    private static List<BookDTO> bookDTOs;

    @BeforeAll
    public static void initBookDTOs() {
        bookDTOs = List.of(
                new BookDTO(1L, "BookTitle_1",
                        new AuthorDTO(1L, "Author_1"),
                        List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2"))),
                new BookDTO(2L, "BookTitle_2",
                        new AuthorDTO(2L, "Author_2"),
                        List.of(new GenreDTO(3L, "Genre_3"), new GenreDTO(4L, "Genre_4"))),
                new BookDTO(3L, "BookTitle_3",
                        new AuthorDTO(3L, "Author_3"),
                        List.of(new GenreDTO(5L, "Genre_5"), new GenreDTO(6L, "Genre_6")))
        );
    }

    @Test
    void shouldReturnCorrectBookById() {
        Optional<BookDTO> actualBookDTO = bookService.findById(1L);
        assertThat(actualBookDTO).isPresent().get().isEqualTo(bookDTOs.get(0));
    }

    @Test
    void whenAllBooksFound() {
        List<BookDTO> actualBookDTOs = bookService.findAll();
        assertThat(actualBookDTOs).isEqualTo(bookDTOs);
    }

    @Test
    @DirtiesContext
    void shouldSaveNewBook() {
        BookDTO actualBookDTO = bookService.insert("new_book", 1L, Set.of(1L, 2L));
        BookDTO expectedBookDTO = new BookDTO(4L, "new_book",
                new AuthorDTO(1L, "Author_1"),
                List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2")));
        assertThat(actualBookDTO).isEqualTo(expectedBookDTO);
    }

    @Test
    @DirtiesContext
    void shouldUpdateBook() {
        BookDTO actualBookDTO = bookService.update(1L, "new_book", 1L, Set.of(1L, 2L));
        BookDTO expectedBookDTO = new BookDTO(1L, "new_book",
                new AuthorDTO(1L, "Author_1"),
                List.of(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2")));
        assertThat(actualBookDTO).isEqualTo(expectedBookDTO);
    }

    @Test
    @DirtiesContext
    void shouldDeleteBook() {
        bookService.deleteById(1L);
        List<BookDTO> actualBookDTOs = bookService.findAll();
        assertThat(actualBookDTOs).isEqualTo(List.of(bookDTOs.get(1), bookDTOs.get(2)));
    }

    @DisplayName("Кидаем EntityNotFoundException")
    @Test
    void throwExceptionBook(){

        assertThrows(EntityNotFoundException.class, () ->  bookService.deleteById(5L));
    }
}