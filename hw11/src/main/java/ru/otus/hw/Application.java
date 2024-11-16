package ru.otus.hw;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws SQLException {

		Console.main(args);
		SpringApplication.run(Application.class, args);
		System.out.printf("%n%s%n%s%n", "http://localhost:8080", "http://localhost:8080/edit/1");
	}

}
