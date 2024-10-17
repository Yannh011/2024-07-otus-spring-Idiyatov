package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;


@DisplayName("Сервис для работы с книгами должен")
@DataMongoTest
@Import(BookServiceImpl.class)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoOperations mongoOperations;

    @DisplayName("вернуть книгу по ее id")
    @Test
    void findById() {
        var firstBookFromDb = mongoOperations.findOne(new Query(), Book.class);

        var book = bookService.findById(firstBookFromDb.getId());

        assertThat(book).isPresent().get().isEqualTo(firstBookFromDb);
    }

    @DisplayName("вернуть все книги")
    @Test
    void findAll() {
        var booksFromDb = mongoOperations.findAll(Book.class);

        var allBooks = bookService.findAll();

        assertThat(allBooks).isEqualTo(booksFromDb);
    }

    @DisplayName("сохранить новую книгу")
    @Test
    @DirtiesContext
    void insert() {
        var author = new Author("1", "Author_1");
        var genre = new Genre("1", "Genre_1");
        var newBook = new Book(null, "newBook", author, genre);
        var countBooksFromDbBeforeInsert = mongoOperations.count(new Query(), Book.class);

        var savedBook = bookService.insert(newBook.getTitle(),
                newBook.getAuthor().getId(), newBook.getGenre().getId());

        assertThat(savedBook)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newBook);

        assertThat(mongoOperations.count(new Query(), Book.class))
                .isEqualTo(countBooksFromDbBeforeInsert + 1);
    }

    @DisplayName("сохранить обновленную книгу и связанные сущности(комменты)")
    @Test
    @DirtiesContext
    void update() {
        var author = new Author("1", "Author_1");
        var genre = new Genre("1", "Genre_1");
        var firstBookFromDb = mongoOperations.findOne(new Query(), Book.class);
        var target = new Book(firstBookFromDb.getId(), "updatedBook", author, genre);
        var query = new Query().addCriteria(Criteria.where("book._id").is(firstBookFromDb.getId()));
        var commentsBeforeUpdateBook = mongoOperations.find(query, Comment.class);

        var updatedBook = bookService.update(firstBookFromDb.getId(), target.getTitle(),
                target.getAuthor().getId(), target.getGenre().getId());

        var commentsAfterUpdateBook = mongoOperations.find(query, Comment.class);

        assertThat(updatedBook).isEqualTo(target);
        assertThat(commentsBeforeUpdateBook).isNotEqualTo(commentsAfterUpdateBook);
        assertThat(commentsBeforeUpdateBook)
                .isNotEmpty()
                .allMatch(comment -> comment.getBook().getTitle().equals(firstBookFromDb.getTitle()));
        assertThat(commentsAfterUpdateBook)
                .isNotEmpty()
                .allMatch(comment -> comment.getBook().getTitle().equals("updatedBook"));
    }

    @DisplayName("удалить книгу по id а также все комменты связанные с ней")
    @Test
    @DirtiesContext
    void deleteById() {
        var deletedBook = mongoOperations.findOne(new Query(), Book.class);
        assertThat(deletedBook).isNotNull();

        var query = new Query().addCriteria(Criteria.where("book._id").is(deletedBook.getId()));
        var commentsBeforeDeleteBook = mongoOperations.find(query, Comment.class);
        assertThat(commentsBeforeDeleteBook).isNotEmpty();

        bookService.deleteById(deletedBook.getId());
        assertThat(mongoOperations.findById(deletedBook.getId(), Book.class)).isNull();

        var commentsAfterDeleteBook = mongoOperations.find(query, Comment.class);
        assertThat(commentsAfterDeleteBook).isEmpty();
    }

    @DisplayName("Кидаем EntityNotFoundException")
    @Test
    void throwExceptionBook(){

        var bookId = "1";
        var updatedAuthorId = "5";
        var updatedGenreId = "2";

        assertThrows(EntityNotFoundException.class, () ->  bookService.update(bookId, anyString(), updatedAuthorId, updatedGenreId));
    }
}
