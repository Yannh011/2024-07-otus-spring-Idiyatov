package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final JdbcOperations jdbc;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Author> findAll() {
        return jdbc.query("SELECT id, full_name FROM authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            Author author = namedParameterJdbcOperations.queryForObject(
                    "SELECT id, full_name FROM authors WHERE id = :id",
                    Map.of("id", id),
                    new AuthorRowMapper());
            return Optional.ofNullable(author);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            long id = resultSet.getLong("id");
            String fullName = resultSet.getString("full_name");
            return new Author(id, fullName);
        }
    }
}