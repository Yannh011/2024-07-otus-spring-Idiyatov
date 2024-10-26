package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

public interface CustomCommentRepository {

    void updateAllCommentsByBook(Book updatedBook);
}
