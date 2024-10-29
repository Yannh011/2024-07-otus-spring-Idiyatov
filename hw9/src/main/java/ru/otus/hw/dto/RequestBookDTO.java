package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBookDTO {

    private long id;

    private String title;

    private long authorId;

    private List<Long> genreIds;

}
