package com.green.blue.red.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NaDTO {

    private Long pno;
    private String pname;
    private int price;
    private String pdesc;
    private List<String> fileNames;


}
