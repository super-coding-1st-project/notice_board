package com.github.supercodingspring.supercodingproject1st.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "datasource")
public class DataSourceProperties {
    private String username;
    private String password;
    private String url;
    private String driverClassName;
}
