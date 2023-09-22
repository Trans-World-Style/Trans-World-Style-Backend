package com.example.transback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
//@EnableAsync
public class AsyncConfig {

//    @Bean
//    public ThreadPoolTaskExecutor asyncExecutor()
    @Bean(name = "asyncExecutor")
    public Executor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1); // 최소 스레드 수
        executor.setMaxPoolSize(2); // 최대 스레드 수
        executor.setQueueCapacity(Integer.MAX_VALUE); // 큐 용량
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
