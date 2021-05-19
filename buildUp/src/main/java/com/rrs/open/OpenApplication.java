package com.rrs.open;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class OpenApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(OpenApplication.class, args);
        context.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> System.out.println("应用程序正在退出..."));
    }
}
