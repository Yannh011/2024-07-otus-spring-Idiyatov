package ru.otus.hw.services.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.AuthorDTO;
import ru.otus.hw.models.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
}
