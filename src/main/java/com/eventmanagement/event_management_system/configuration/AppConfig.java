package com.eventmanagement.event_management_system.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.sql.DataSource;
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

            // 1. عدد العمال الأساسي (شغالين دائماً)
            executor.setCorePoolSize(5);

            // 2. أقصى عدد عمال ممكن يوصله السيرفر وقت الزحمة
            executor.setMaxPoolSize(10);

            // 3. حجم الطابور (لو الـ 10 مشغولين، الـ 100 طلب هذول بستنوا دورهم)
            executor.setQueueCapacity(100);

            // 4. اسم الخيوط (عشان لما تعمل Debug تعرف إنه هاد تبع الإيميل)
            executor.setThreadNamePrefix("Email-Thread-");

            executor.initialize();
            return executor;
        }
    }

}
