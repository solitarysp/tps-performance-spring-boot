package com.lethanh98.performance.tps.springboot.config.config;

import com.lethanh98.performance.tps.GroupTpsCounter;
import com.lethanh98.performance.tps.TpsCounter;
import com.lethanh98.performance.tps.springboot.config.aop.annotation.TpsCountTraceAspect;
import com.lethanh98.performance.tps.springboot.config.config.properties.TpsCountConfigProperties;
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
public class TpsCountService {
    @Autowired(required = false)
    TpsCountConfigProperties tpsCountConfigProperties;
    @Getter
    Map<String, TpsCounter> singleton = new HashMap<>();
    @Getter
    Map<String, GroupTpsCounter> multiple = new HashMap<>();

    @Async("threadPoolAddTpsCount")
    public void addTps(TpsCountTraceAspect tpsCountTraceAspect) {
        try {
            if (!tpsCountTraceAspect.isMultiple()) {
                TpsCounter tpsCounter = getSingleton().get(tpsCountTraceAspect.name());
                if (Objects.nonNull(tpsCounter)) {
                    tpsCounter.addTps();
                } else {
                    log.warn("Tps count not found {}", tpsCountTraceAspect.name());
                }
            } else {
                GroupTpsCounter groupTpsCounter = getMultiple().get(tpsCountTraceAspect.name());
                if(Objects.nonNull(groupTpsCounter)){
                    groupTpsCounter.addTps();
                }else {
                    log.warn("Tps count not found group {}", tpsCountTraceAspect.name());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Autowired
    private void initSingleton() {
        if (Objects.nonNull(tpsCountConfigProperties)) {
            tpsCountConfigProperties.getSingletonConfig().entrySet().forEach(stringTpsConfigEntry -> {
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
    private void initMultiple() {
        if (Objects.nonNull(tpsCountConfigProperties)) {
            tpsCountConfigProperties.getMultipleConfig().entrySet().forEach(multipleTpsConfigEntry -> {
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
