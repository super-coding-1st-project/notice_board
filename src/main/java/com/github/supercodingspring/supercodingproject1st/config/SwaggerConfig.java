package com.github.supercodingspring.supercodingproject1st.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
//    @Bean
//    public OpenAPI openAPI() {
//
//        Info info = new Info()
//                .version("v1.0.0")
//                .title("전시로그 API")
//                .description("전시로그 API 목록입니다.");
//
//        return new OpenAPI()
//                .info(info);
//    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
