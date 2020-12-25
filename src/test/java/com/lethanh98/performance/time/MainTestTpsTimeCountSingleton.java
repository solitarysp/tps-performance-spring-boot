package com.lethanh98.performance.time;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"application.properties"})
public class MainTestTpsTimeCountSingleton {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    TestTpsTimeCountSingleton testTpsMultipleService;
    @Test
    public void testTpsMultiple() throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            testTpsMultipleService.test();
        }
    }
}
