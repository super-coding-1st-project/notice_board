package com.github.supercodingspring.supercodingproject1st.web.advice;

import com.github.supercodingspring.supercodingproject1st.service.exception.InvalidValueException;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotAcceptException;
import com.github.supercodingspring.supercodingproject1st.service.exception.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.sasl.AuthenticationException;
import java.security.SignatureException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException nfe){
        log.error("Client 요청 이후 DB 검색 중 에러 발생 "+nfe.getMessage());
        return nfe.getMessage();
    }
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(NotAcceptException.class)
    public String handleNotAcceptException(NotAcceptException nae){
        log.error("Client 요청 이후 DB 검색 중 Not Acceptable 에러 발생 "+nae.getMessage());
        return nae.getMessage();
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidValueException.class)
    public String handleInvalidValueException(InvalidValueException ive){
        log.error("Client 요청에 문제가 있어 다음처럼 출력합니다. " + ive.getMessage());
        return ive.getMessage();
    }
}
