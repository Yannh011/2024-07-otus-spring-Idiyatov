package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDTO;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.mappers.GenreMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDTO> findAll() {
        return genreMapper.toDto(genreRepository.findAll());
    }
}
