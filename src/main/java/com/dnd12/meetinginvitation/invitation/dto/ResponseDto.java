package com.dnd12.meetinginvitation.invitation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    private String result;
    private String message;
    private List<?> data;

    public static ResponseDto success(List<?> data){
        return new ResponseDto("success" , "", data);
    }

    public static ResponseDto fail(String message){
        return new ResponseDto("fail" , message, Collections.singletonList(""));
    }
}
