package ru.otus.hw1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {

    @GetMapping("/sometext")
    public String f() {
        return "Some text";
    }
}
