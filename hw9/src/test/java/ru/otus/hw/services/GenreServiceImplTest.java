package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.services.mappers.GenreMapperImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({GenreServiceImpl.class, GenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class GenreServiceImplTest {

    @Autowired
    private GenreService genreService;

    @Test
    void whenAllGenresFound() {
        List<GenreDTO> expectedGenreDTOs = List.of(
                new GenreDTO(1L, "Genre_1"),
                new GenreDTO(2L, "Genre_2"),
                new GenreDTO(3L, "Genre_3"),
                new GenreDTO(4L, "Genre_4"),
                new GenreDTO(5L, "Genre_5"),
                new GenreDTO(6L, "Genre_6")
        );
        List<GenreDTO> actualGenreDTOs = genreService.findAll();
        assertEquals(expectedGenreDTOs, actualGenreDTOs);
    }
}