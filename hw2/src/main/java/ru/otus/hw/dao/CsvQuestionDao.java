package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        ClassPathResource resource = new ClassPathResource(fileNameProvider.getTestFileName());
        try (InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            CsvToBean<QuestionDto> cb = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build();

            return cb.parse().stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();

        } catch (Exception e) {
            throw new QuestionReadException("Error reading from file", e);
        }
    }
}
