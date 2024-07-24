package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("scms")
public class Test {
    @GetMapping("getString")
    public String getMesssage(){
        return "abc";
    }

}
