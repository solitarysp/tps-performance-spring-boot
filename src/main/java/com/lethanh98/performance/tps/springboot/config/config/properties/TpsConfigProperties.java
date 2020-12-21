package com.lethanh98.performance.tps.springboot.config.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "app.tps")
@Configuration
@Data
public class TpsConfigProperties {
    boolean enabled = false;
    ThreadTaskConfig addTpsThread = new ThreadTaskConfig();
    Map<String, TpsConfig> singletonConfig = new Hashtable<>();
    Map<String, MultipleTpsConfig> multipleConfig = new Hashtable<>();

    @Data
    public static class TpsConfig {
        TimeUnit timeUnit = TimeUnit.MICROSECONDS;
        private long duration = 10000;
        private String msg = "%tps%";
        private String name;
    }

    @Data
    public static class MultipleTpsConfig {
        List<TpsConfig> tpsConfigs = new ArrayList<>();
    }

    @Data
    public static class ThreadTaskConfig {
        int corePoolSize = 10;
        int maxPoolSize = 15;
        String threadNamePrefix = "Default";
    }
}
