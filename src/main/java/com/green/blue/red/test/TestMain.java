package com.green.blue.red.test;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class TestMain {

    public static void main(String[] args) {
        log.info("사랑");
        Map<String, Long> map = new HashMap<>();

        for(int i=0; i<10; i++) {
            map.put("a" + i, (long)((i+1)*5));
        }

        Thread thread1 = new MyThread();
        thread1.setName("Thread #1");

        Thread thread2 = new MyThread();
        thread2.setName("Thread #2");

        Runnable runnable1 = new MyRunnable();
        Runnable runnable2 = new MyRunnable();

        Thread thread3 = new Thread(runnable1);
        thread3.setName("Thread #3");

        Thread thread4 = new Thread(runnable2);
        thread4.setName("Thread #4");

//        thread1.start();
//        thread2.start();
//        thread3.start();
//        thread4.start();

        //배열이나 리스트에 저장하고 start 를 시켜서 실행해

        List<Thread> list = new ArrayList<>();
        list.add(thread1);
        list.add(thread2);
        list.add(thread3);
        list.add(thread4);



    }
}
