package com.lethanh98.performance.tps.springboot.config.config;

import com.lethanh98.performance.tps.GroupTpsCounter;
import com.lethanh98.performance.tps.TpsCounter;
import com.lethanh98.performance.tps.springboot.config.aop.annotation.TpsTraceAspect;
import com.lethanh98.performance.tps.springboot.config.config.properties.TpsConfigProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@Slf4j
public class TpsService {
    @Autowired(required = false)
    TpsConfigProperties tpsConfigProperties;
    @Getter
    Map<String, TpsCounter> singleton = new HashMap<>();
    @Getter
    Map<String, GroupTpsCounter> multiple = new HashMap<>();

    @Async("threadPoolAddTps")
    public void addTps(TpsTraceAspect tpsTraceAspect) {
        try {
            if (!tpsTraceAspect.isMultiple()) {
                getSingleton().get(tpsTraceAspect.name()).addTps();
            } else {
                getMultiple().get(tpsTraceAspect.name()).addTps();
            }
        } catch (Exception e) {

        }
    }

    @Autowired
    public void initSingleton() {
        if (Objects.nonNull(tpsConfigProperties)) {
            tpsConfigProperties.getSingletonConfig().entrySet().forEach(stringTpsConfigEntry -> {
                TpsCounter tpsCounter = new TpsCounter(
                        stringTpsConfigEntry.getKey(),
                        stringTpsConfigEntry.getValue().getDuration(),
                        stringTpsConfigEntry.getValue().getTimeUnit(), (s, l) -> {
                    log.info(stringTpsConfigEntry.getValue().getMsg().replace("%tps%", String.valueOf(l)));
                });
                singleton.put(tpsCounter.getName(), tpsCounter);
            });
        }
    }

    @Autowired
    public void initMultiple() {
        if (Objects.nonNull(tpsConfigProperties)) {
            tpsConfigProperties.getMultipleConfig().entrySet().forEach(multipleTpsConfigEntry -> {
                List<TpsCounter> tpsCounters = new ArrayList<>();

                multipleTpsConfigEntry.getValue().getTpsConfigs().forEach(stringTpsConfigEntry -> {
                    TpsCounter tpsCounter = new TpsCounter(
                            stringTpsConfigEntry.getName(),
                            stringTpsConfigEntry.getDuration(),
                            stringTpsConfigEntry.getTimeUnit(), (s, l) -> {
                        log.info(stringTpsConfigEntry.getMsg().replace("%tps%", String.valueOf(l)));
                    });
                    tpsCounters.add(tpsCounter);
                });
                GroupTpsCounter groupTpsCounter = new GroupTpsCounter(multipleTpsConfigEntry.getKey(), tpsCounters);
                multiple.put(multipleTpsConfigEntry.getKey(), groupTpsCounter);
            });
        }
    }
}
