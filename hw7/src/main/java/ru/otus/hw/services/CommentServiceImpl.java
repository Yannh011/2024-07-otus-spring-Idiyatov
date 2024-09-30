package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(long id) {

        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment insert(String text, long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с ID " + bookId + " не найдена."));
        return save(0, text, book);
    }

    private Comment save(long commentId, String text, Book book) {
        Comment comment = new Comment(commentId, text, book);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(long id, String text, long bookId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с ID " + id + " не найден."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Книга с ID " + bookId + " не найдена."));

        comment.setText(text);
        comment.setBook(book);

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Comment> findAllByBookId(long id) {
        bookRepository.findById(id);
        return commentRepository.findAllByBookId(id);
    }
}
