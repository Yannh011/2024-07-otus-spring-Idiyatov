package ru.otus.hw.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.models.Comment;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Override
    @Mapping(source = "bookId", target = "book.id")
    Comment toEntity(CommentDTO dto);

    @Override
    @Mapping(source = "book.id", target = "bookId")
    CommentDTO toDto(Comment entity);

    @Override
    @Mapping(source = "bookId", target = "book.id")
    List<Comment> toEntity(List<CommentDTO> dtoList);

    @Override
    @Mapping(source = "book.id", target = "bookId")
    List<CommentDTO> toDto(List<Comment> entityList);
}
