package com.green.blue.red.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("")
    public String home(){
        System.out.println("사랑");

        return "사랑";
    }
}
