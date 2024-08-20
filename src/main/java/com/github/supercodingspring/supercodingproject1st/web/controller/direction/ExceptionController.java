package com.github.supercodingspring.supercodingproject1st.web.controller.direction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exceptions")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entrypointException() {
        throw new RuntimeException("에러");
//        throw new CAuthenticationEntryPointException( "인증 과정에서 에러");
    }

    @GetMapping(value = "/access-denied")
    public void accessDeniedException() {
        throw new RuntimeException("에러");
//        throw new AccessDeniedException("");
    }
}