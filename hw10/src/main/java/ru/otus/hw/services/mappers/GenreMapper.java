package ru.otus.hw.services.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {
}
