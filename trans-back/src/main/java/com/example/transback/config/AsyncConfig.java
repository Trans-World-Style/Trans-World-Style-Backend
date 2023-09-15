package com.example.transback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // 최소 스레드 수
        executor.setMaxPoolSize(3); // 최대 스레드 수
        executor.setQueueCapacity(Integer.MAX_VALUE); // 큐 용량
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
