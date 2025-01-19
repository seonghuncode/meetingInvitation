package com.dnd12.meetinginvitation.domain.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {

    //http://localhost:8080/test
    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }
}