package ru.otus.hw.services.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.models.Book;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper extends EntityMapper<BookDTO, Book> {
}
