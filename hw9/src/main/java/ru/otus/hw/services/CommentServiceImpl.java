package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.mappers.CommentMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDTO> findById(long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(commentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> findAllByBookId(long bookId) {
        return commentMapper.toDto(commentRepository.findByBookId(bookId));
    }

    @Override
    @Transactional
    public CommentDTO insert(String text, long bookId) {
        return commentMapper.toDto(save(0, text, bookId));
    }

    @Override
    @Transactional
    public CommentDTO update(long id, String text, long bookId) {
        return commentMapper.toDto(save(id, text, bookId));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comment = new Comment(id, text, book);
        return commentRepository.save(comment);
    }

}
