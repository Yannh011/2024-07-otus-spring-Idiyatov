package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@ChangeLog(order = "000")
public class InitMongoDBDataChangeLog {

    private Author author1;

    private Author author2;

    private Author author3;

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Book book1;

    private Book book2;

    private Book book3;

    private Comment comment1;

    private Comment comment2;

    private Comment comment3;

    private Comment comment4;

    private Comment comment5;

    private Comment comment6;

    @ChangeSet(order = "000", id = "dropDB", runAlways = true, author = "idiyatov_yan")
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", runAlways = true, author = "idiyatov_yan")
    public void initAuthors(AuthorRepository repository) {
        author1 = repository.save(new Author("1","Author_1"));
        author2 = repository.save(new Author("2","Author_2"));
        author3 = repository.save(new Author("3","Author_3"));
    }

    @ChangeSet(order = "002", id = "initGenres", runAlways = true, author = "idiyatov_yan")
    public void initGenres(GenreRepository repository) {
        genre1 = repository.save(new Genre("1","Genre_1"));
        genre2 = repository.save(new Genre("2","Genre_2"));
        genre3 = repository.save(new Genre("3","Genre_3"));
    }

    @ChangeSet(order = "003", id = "initBooks", runAlways = true, author = "idiyatov_yan")
    public void initBooks(BookRepository repository) {
        book1 = repository.save(new Book("1","BookTitle_1", author1, genre1));
        book2 = repository.save(new Book("2","BookTitle_2", author2, genre2));
        book3 = repository.save(new Book("3","BookTitle_3", author3, genre3));
    }

    @ChangeSet(order = "004", id = "initComments", runAlways = true, author = "idiyatov_yan")
    public void initComments(CommentRepository repository) {
        comment1 = repository.save(new Comment("1","Comment_1_1", book1));
        comment2 = repository.save(new Comment("2","Comment_1_2", book1));
        comment3 = repository.save(new Comment("3","Comment_2_1", book2));
        comment4 = repository.save(new Comment("4","Comment_2_2", book2));
        comment5 = repository.save(new Comment("5","Comment_3_1", book3));
        comment6 = repository.save(new Comment("6","Comment_3_2", book3));
    }
}
