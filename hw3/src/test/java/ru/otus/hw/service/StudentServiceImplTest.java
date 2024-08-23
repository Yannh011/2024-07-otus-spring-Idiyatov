package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private IOService ioService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void determineCurrentStudent_ReturnsCorrectStudent() {
        String firstName = "John";
        String lastName = "Doe";
        when(ioService.readStringWithPrompt("Please input your first name")).thenReturn(firstName);
        when(ioService.readStringWithPrompt("Please input your last name")).thenReturn(lastName);

        Student student = studentService.determineCurrentStudent();

        assertEquals(firstName, student.firstName());
        assertEquals(lastName, student.lastName());
    }
}