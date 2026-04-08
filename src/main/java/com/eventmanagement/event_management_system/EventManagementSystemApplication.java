package com.eventmanagement.event_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

@SpringBootApplication
public class EventManagementSystemApplication {
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> customMessageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

	public static void main(String[] args) {

		SpringApplication.run(EventManagementSystemApplication.class, args);
	}

}
