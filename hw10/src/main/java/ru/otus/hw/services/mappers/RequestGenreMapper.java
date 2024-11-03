package ru.otus.hw.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.otus.hw.models.Genre;

@Mapper(componentModel = "spring")
public interface RequestGenreMapper {

    @Named("mapToGenre")
    default Genre mapToGenre(Long id) {
        if (id == null) {
            return null;
        }
        Genre genre = new Genre();
        genre.setId(id);
        return genre;
    }

    @Named("mapToLong")
    default Long mapToLong(Genre genre) {
        return genre.getId();
    }

}
