package com.lethanh98.performance.tps.springboot.config.config;

import com.lethanh98.performance.tps.springboot.config.aop.annotation.TpsTimeCountTraceAspect;
import com.lethanh98.performance.tps.springboot.config.config.properties.TpsTimeCountConfigProperties;
import com.lethanh98.performance.tps.time.GroupTpsTimeCounter;
import com.lethanh98.performance.tps.time.TpsTimeCounter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class TpsTimeCountService {
    @Autowired
    TpsTimeCountConfigProperties tpsTimeCountConfigProperties;
    @Getter
    Map<String, TpsTimeCounter> singleton = new ConcurrentHashMap<>();
    @Getter
    Map<String, GroupTpsTimeCounter> multiple = new ConcurrentHashMap<>();

    @Async("threadPoolAddTpsCount")
    public void addTps(TpsTimeCountTraceAspect tpsTimeCountTraceAspect, long time) {
        try {
            if (!tpsTimeCountTraceAspect.isMultiple()) {
                TpsTimeCounter tpsCounter = getSingleton().get(tpsTimeCountTraceAspect.name());
                if (Objects.nonNull(tpsCounter)) {
                    tpsCounter.addTps(time);
                } else {
                    log.warn("Tps time count not found {}", tpsTimeCountTraceAspect.name());
                }
            } else {
                GroupTpsTimeCounter groupTpsCounter = getMultiple().get(tpsTimeCountTraceAspect.name());
                if (Objects.nonNull(groupTpsCounter)) {
                    groupTpsCounter.addTps(time);
                } else {
                    log.warn("Tps time count not found group {}", tpsTimeCountTraceAspect.name());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Autowired
    private void initSingleton() {
        if (Objects.nonNull(tpsTimeCountConfigProperties)) {
            tpsTimeCountConfigProperties.getSingletonConfig().entrySet().forEach(stringTpsConfigEntry -> {
                TpsTimeCounter tpsCounter = new TpsTimeCounter(
                        stringTpsConfigEntry.getKey(),
                        stringTpsConfigEntry.getValue().getDuration(),
                        stringTpsConfigEntry.getValue().getTimeUnit(), (s, l) -> {

                    String result = stringTpsConfigEntry.getValue().getMsg().replace("%tps%", String.valueOf(stringTpsConfigEntry.getValue().getTypeTimeTps().convert(l, TimeUnit.NANOSECONDS)));
                    result = result.replace("%type-time%", stringTpsConfigEntry.getValue().getTypeTimeTps().name());
                    log.info(result);
                });
                singleton.put(tpsCounter.getName(), tpsCounter);
            });
        }
    }

    @Autowired
    private void initMultiple() {
        if (Objects.nonNull(tpsTimeCountConfigProperties)) {
            tpsTimeCountConfigProperties.getMultipleConfig().entrySet().forEach(multipleTpsConfigEntry -> {
                List<TpsTimeCounter> tpsTimeCounters = new ArrayList<>();

                multipleTpsConfigEntry.getValue().getTpsConfigs().forEach(stringTpsConfigEntry -> {
                    TpsTimeCounter tpsCounter = new TpsTimeCounter(
                            stringTpsConfigEntry.getName(),
                            stringTpsConfigEntry.getDuration(),
                            stringTpsConfigEntry.getTimeUnit(), (s, l) -> {

                        String result = stringTpsConfigEntry.getMsg().replace("%tps%",
                                String.valueOf(stringTpsConfigEntry.getTypeTimeTps().convert(l, TimeUnit.NANOSECONDS)));

                        result = result.replace("%type-time%", stringTpsConfigEntry.getTypeTimeTps().name());
                        log.info(result);
                        log.info(stringTpsConfigEntry.getMsg().replace("%tps%",
                                String.valueOf(stringTpsConfigEntry.getTypeTimeTps().convert(l, TimeUnit.NANOSECONDS))));
                    });
                    tpsTimeCounters.add(tpsCounter);
                });
                GroupTpsTimeCounter groupTpsCounter = new GroupTpsTimeCounter(multipleTpsConfigEntry.getKey(), tpsTimeCounters);
                multiple.put(multipleTpsConfigEntry.getKey(), groupTpsCounter);
            });
        }
    }
}
