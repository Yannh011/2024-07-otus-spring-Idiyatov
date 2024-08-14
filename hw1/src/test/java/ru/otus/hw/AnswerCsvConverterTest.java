package ru.otus.hw;

import org.junit.jupiter.api.Test;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.dao.dto.AnswerCsvConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AnswerCsvConverterTest {

    private final AnswerCsvConverter answerCsvConverter = new AnswerCsvConverter();

    public static final String ANSWER_TEXT = "Check Anything";
    public static final boolean IS_CORRECT = false;
    public static final String CSV_ANSWER = ANSWER_TEXT + "%" + IS_CORRECT;

    @Test
    void correctConverting() {

        Answer answer = (Answer) answerCsvConverter.convertToRead(CSV_ANSWER);
        assertEquals(ANSWER_TEXT, answer.text());
        assertEquals(IS_CORRECT, answer.isCorrect());
    }
}
