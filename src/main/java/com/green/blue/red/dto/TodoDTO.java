package com.green.blue.red.dto;

import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class TodoDTO {
    private Long tno;

    private String title;
    private  String writer;
    private boolean complete;
    private LocalDate dueDate;
}
