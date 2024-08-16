package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {

    private CsvQuestionDao csvQuestionDao;
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    public void setUp() {
        fileNameProvider = Mockito.mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn("test-questions.csv");
        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }

    @Test
    void testFindAll() {
        List<Question> questions = csvQuestionDao.findAll();
        assertNotNull(questions);
        assertEquals(1, questions.size());
    }

    @Test
    void testFileNotFound() {
        when(fileNameProvider.getTestFileName()).thenReturn("nonexistent-file.csv");

        QuestionReadException exception = assertThrows(QuestionReadException.class, () -> {
            csvQuestionDao.findAll();
        });

        assertTrue(exception.getMessage().contains("Error reading from file"));
    }
}