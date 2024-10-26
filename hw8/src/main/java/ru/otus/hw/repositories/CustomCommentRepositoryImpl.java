package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void updateAllCommentsByBook(Book updatedBook) {
        Query query = new Query(where("book.id").is(updatedBook.getId()));
        Update update = new Update().set("book", updatedBook);
        mongoTemplate.updateMulti(query, update, Comment.class);
    }
}
