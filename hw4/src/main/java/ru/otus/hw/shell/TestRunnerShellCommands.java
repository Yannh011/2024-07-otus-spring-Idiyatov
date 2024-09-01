package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestRunnerShellCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run tests!", key = {"r", "run"})
    public void runTests() {
        testRunnerService.run();
    }
}
