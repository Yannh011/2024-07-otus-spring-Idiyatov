package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDTO;
import ru.otus.hw.dto.RequestBookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.mappers.BookMapper;
import ru.otus.hw.services.mappers.RequestBookMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final RequestBookMapper requestBookMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDTO> findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RequestBookDTO> findBookById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(requestBookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDto(books);
    }

    @Override
    @Transactional
    public BookDTO insert(String title, long authorId, Set<Long> genresIds) {
        return bookMapper.toDto(save(0, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public BookDTO update(long id, String title, long authorId, Set<Long> genresIds) {
        return bookMapper.toDto(save(id, title, authorId, genresIds));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findByIdIn(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
