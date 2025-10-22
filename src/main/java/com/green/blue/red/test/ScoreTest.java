package com.green.blue.red.test;

import com.green.blue.red.test.obj.ScoreVO;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreTest {

    public static void main(String[] args) {

        String[][] data = {
                {"1","1","30","50","70","홍길동"},
                {"1","2","50","98","99","김길동"},
                {"1","3","70","88","66","김말자"},
                {"2","1","100","80","70","권기현"},
                {"2","2","66","55","95","전별"},
                {"2","3","77","100","88","김유라"},
                {"3","1","55","77","66","조형우"},
                {"3","2","100","50","40","신소라"},
                {"3","3","20","10","90","임병국"},
        };

        Map<String, Map<String, ScoreVO>> scoreMap = new LinkedHashMap<>();

        for(int i=0; i<data.length; i++) {
                ScoreVO score = new ScoreVO();
                score.setCno(Integer.parseInt(data[i][0]));
                score.setSno(Integer.parseInt(data[i][1]));
                score.setName(data[i][5]);
                score.setKor(Integer.parseInt(data[i][2]));
                score.setEng(Integer.parseInt(data[i][3]));
                score.setMath(Integer.parseInt(data[i][4]));
                score.setTotal(score.getKor()+score.getEng()+score.getMath());
                score.setAvg(score.getTotal()/3.0f);
                score.setGrade(ScoreVO.calcGrade(score.getAvg()));

            Map<String, ScoreVO> classMap = scoreMap.computeIfAbsent(score.getCno() + "반", key -> {
                return new LinkedHashMap<>();
            });
            classMap.put(score.getSno()+"번", score);
        }
        System.out.println("scoreMap = " + scoreMap);

    }
}
