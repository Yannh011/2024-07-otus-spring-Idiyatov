package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        try {
            Book book = namedParameterJdbcOperations.
                    queryForObject("SELECT b.ID, b.TITLE, b.author_id, b.genre_id, " +
                                    "a.FULL_NAME AS author_name, g.name AS genre_name " +
                                    "FROM BOOKS b " +
                                    "LEFT JOIN AUTHORS a ON b.author_id = a.id " +
                                    "LEFT JOIN GENRES g ON b.genre_id = g.id " +
                                    "WHERE b.id = :id",
                            Map.of("id", id), new BookRowMapper());

            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("SELECT b.ID, b.TITLE, b.author_id, b.genre_id, " +
                        "a.FULL_NAME AS author_name, g.name AS genre_name " +
                        "FROM BOOKS b " +
                        "LEFT JOIN AUTHORS a ON b.author_id = a.id " +
                        "LEFT JOIN GENRES g ON b.genre_id = g.id",
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        int affectedRows = namedParameterJdbcOperations.update("DELETE FROM BOOKS WHERE ID = :id",
                Map.of("id", id));
        if (affectedRows == 0) {
            throw new EntityNotFoundException("No record found with id: " + id);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "INSERT INTO BOOKS (TITLE, AUTHOR_ID, GENRE_ID) VALUES (:title, :authorId, :genreId)",
                new MapSqlParameterSource()
                        .addValues(Map.of(
                                "title", book.getTitle(),
                                "authorId", book.getAuthor().getId(),
                                "genreId", book.getGenre().getId())),
                keyHolder
        );
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        int affectedRows = namedParameterJdbcOperations.update(
                "UPDATE BOOKS SET TITLE = :title, AUTHOR_ID = :authorId, GENRE_ID = :genreId WHERE ID = :id",
                Map.of(
                        "id", book.getId(),
                        "title", book.getTitle(),
                        "authorId", book.getAuthor().getId(),
                        "genreId", book.getGenre().getId()
                )
        );
        if (affectedRows == 0) {
            throw new EntityNotFoundException("No record found with id: " + book.getId());
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            Author author = new Author(resultSet.getLong("author_id"),
                    resultSet.getString("author_name"));
            Genre genre = new Genre(resultSet.getLong("genre_id"),
                    resultSet.getString("genre_name"));
            return new Book(id, title, author, genre);
        }
    }
}
