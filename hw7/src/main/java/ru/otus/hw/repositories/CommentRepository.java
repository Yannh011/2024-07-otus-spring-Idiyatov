package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"book.author", "book.genre"})
    Optional<Comment> findById(long id);

    @EntityGraph(attributePaths = {"book.author", "book.genre"})
    List<Comment> findAllByBookId(long bookId);
}
