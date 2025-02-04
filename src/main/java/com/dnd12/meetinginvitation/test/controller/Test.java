package com.dnd12.meetinginvitation.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Test {

    //http://localhost:8080/test1
    @Operation(summary = "테스트", description = "서버 연결 테스트")
    @ApiResponse(responseCode = "200", description = "테스트 성공")
    @RequestMapping(value="/test1", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "test";
    }
}