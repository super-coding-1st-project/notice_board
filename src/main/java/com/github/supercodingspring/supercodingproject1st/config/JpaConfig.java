package com.github.supercodingspring.supercodingproject1st.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.github.supercodingspring.supercodingproject1st.repository",
        entityManagerFactoryRef = "localContainerEntityManagerFactoryBean",
        transactionManagerRef = "tmJpa"
)
public class JpaConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(@Qualifier("dataSource1") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.github.supercodingspring.supercodingproject1st.repository");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.use_sql_comments", true);

        em.setJpaPropertyMap(properties);
        return em;

    }

    @Bean(name = "tmJpa")
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource1") DataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean(dataSource).getObject());
        return transactionManager;
    }

}
