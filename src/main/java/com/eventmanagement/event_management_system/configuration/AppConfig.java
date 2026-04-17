package com.eventmanagement.event_management_system.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        return new WebServiceTemplate();
    }

    @Configuration
    @EnableAsync
    public class AsyncConfig {

        @Bean(name = "mailExecutor")
        public Executor mailExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(5);
            executor.setMaxPoolSize(10);
            executor.setQueueCapacity(100);
            executor.setThreadNamePrefix("Email-Thread-");

            executor.initialize();
            return executor;
        }
    }

}
