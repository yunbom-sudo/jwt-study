package com.example.king.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    DUPLICATE_USERNAME(
            HttpStatus.CONFLICT,
            "이미 존재하는 유저네임 입니다."
    ),

    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "사용자를 찾을수 없습니다."
    ),

    INVALID_PASSWORD(
            HttpStatus.BAD_REQUEST,
            "비밀번호가 일치하지 않습니다."
    ),

    INVALID_REFRESH_TOKEN(
            HttpStatus.BAD_REQUEST,
            "로그인 세션이 만료되었습니다."
    );


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status,String message){
        this.status = status;
        this.message = message;
    }

}
