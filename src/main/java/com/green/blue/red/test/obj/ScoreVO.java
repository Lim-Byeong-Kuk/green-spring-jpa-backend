package com.green.blue.red.test.obj;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ScoreVO {

    private String name;
    private int sno;
    private int cno;
    private int kor;
    private int eng;
    private int math;
    private int total;
    private float avg;
    private String grade;

    public static String calcGrade(float avg) {
        String grade = "";
        if(avg>=90) {
            grade = "수";
        } else if(avg>=80) {
            grade = "우";
        } else if(avg>=70) {
            grade = "미";
        } else if(avg>=60) {
            grade = "양";
        } else {
            grade = "가";
        }
        return grade;
    }
}
