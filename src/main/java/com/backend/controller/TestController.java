package com.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/home")
    public String helloGuest() {
        return "hello guest";
    }

    @GetMapping("/user/home")
    public String helloUser() {
        return "hello user";
    }

}