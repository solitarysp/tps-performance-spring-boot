package com.lethanh98.performance.tps.springboot.config.config;

import com.lethanh98.performance.tps.springboot.config.config.properties.TpsConfigProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Slf4j
@Getter
public class TpsAsyncConfig {
    @Autowired(required = false)
    TpsConfigProperties tpsConfigProperties;

    @Bean("threadPoolAddTps")
    public TaskExecutor threadPoolAddTps() {
        log.info(tpsConfigProperties.getAddTpsThread().getThreadNamePrefix() + ": corePoolSize : " + tpsConfigProperties.getAddTpsThread().getCorePoolSize() + ", MaxPoolSize : " + tpsConfigProperties.getAddTpsThread().getMaxPoolSize());
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(tpsConfigProperties.getAddTpsThread().getCorePoolSize());
        executor.setMaxPoolSize(tpsConfigProperties.getAddTpsThread().getMaxPoolSize());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix(tpsConfigProperties.getAddTpsThread().getThreadNamePrefix());
        return executor;
    }
}
