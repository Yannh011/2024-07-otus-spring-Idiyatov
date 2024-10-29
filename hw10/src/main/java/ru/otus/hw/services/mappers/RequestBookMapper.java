package ru.otus.hw.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.RequestBookDTO;
import ru.otus.hw.models.Book;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, RequestGenreMapper.class})
public interface RequestBookMapper {

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "genreIds", target = "genres", qualifiedByName = "mapToGenre")
    Book toEntity(RequestBookDTO dto);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "genres", target = "genreIds", qualifiedByName = "mapToLong")
    RequestBookDTO toDto(Book entity);

}
