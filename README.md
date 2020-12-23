# tps-performance-spring-boot
# TPS enabled
 - app.tps.enabled=true
# Common config
- `msg` is msg show log
- `duration` is duration tracking
- `time-unit` type time tracking
## TPS singleton
- Use to create a tps tracking. Config in `application.properties`
```
app.tps.singleton-config.routersingleton.msg= Tps routerSingleton in 5s  is %tps% with total %total%
app.tps.singleton-config.routersingleton.duration=5
app.tps.singleton-config.routersingleton.time-unit=seconds
```
- `routersingleton` is name singleton
- msg for log.
  - %tps% is log data binding to tps
## Class using with annotation

```
@Service
@Slf4j
public class TestTpsSingletonService {
    @TpsTraceAspect(name = "routersingleton")
    public void test() {
        log.info("router ++");
    }
}
```
- Using `@TpsTraceAspect` with ``name`` mapping with config
## Test
```
   @Test
    public void testTpsSingleton() throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            singletonService.test();
        }
        Thread.sleep(11000);
    }
```

## Result
```
count router ++ 5000
[Timer-0] INFO com.lethanh98.performance.tps.springboot.config.config.TpsService - Tps routerSingleton in 5s  is 1000 with total 5000
```

## TPS Multiple
- Use to create a set of tps tracking. Config in `application.properties`
```
app.tps.multiple-config.router.tps-configs[0].msg= Tps router in 5s  is %tps% with total %total%
app.tps.multiple-config.router.tps-configs[0].duration=5
app.tps.multiple-config.router.tps-configs[0].time-unit=seconds
app.tps.multiple-config.router.tps-configs[1].msg= Tps router in 10s  is %tps% with total %total%
app.tps.multiple-config.router.tps-configs[1].duration=10
app.tps.multiple-config.router.tps-configs[1].time-unit=seconds
```
- `router` is name Multiple
- [0] and [1] ... is set tps tracking
- msg for log. 
    - %tps% is log data binding to tps
    - %total% is total in duration  binding
## Class using with annotation
```
@Service
@Slf4j
public class TestTpsMultipleService {
    @TpsTraceAspect(name = "router", isMultiple = true)
    public void test() {
        log.info("router ++");
    }
}
```
- Using `@TpsTraceAspect` with ``name`` is Multiple and `isMultiple` is isMultiple
## Test

```
   @Test
    public void testTpsMultiple() throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            testTpsMultipleService.test();
        }
        Thread.sleep(11000);
    }
```

## Result
```
count router ++ 5000

[Timer-0] INFO com.lethanh98.performance.tps.springboot.config.config.TpsService - Tps router in 5s  is 1000 with total 5000
[Timer-1] INFO com.lethanh98.performance.tps.springboot.config.config.TpsService - Tps router in 10s  is 500 with total 5000
```