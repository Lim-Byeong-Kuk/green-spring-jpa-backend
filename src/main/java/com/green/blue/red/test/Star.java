package com.green.blue.red.test;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Star {

    private int math;
    private int eng;
    private int no;

    public Star(int math, int eng) {
        this.math = math;
        this.eng = eng;
    }

}
