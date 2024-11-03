package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.services.mappers.AuthorMapperImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({AuthorServiceImpl.class, AuthorMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @Test
    void whenAllAuthorsFound() {
        List<AuthorDTO> expectedAuthorDTOs = List.of(
                new AuthorDTO(1L, "Author_1"),
                new AuthorDTO(2L, "Author_2"),
                new AuthorDTO(3L, "Author_3")
        );
        List<AuthorDTO> actualAuthorDTOs = authorService.findAll();
        assertEquals(expectedAuthorDTOs, actualAuthorDTOs);
    }

}