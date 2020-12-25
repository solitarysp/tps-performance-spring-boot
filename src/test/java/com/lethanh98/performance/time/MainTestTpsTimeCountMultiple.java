package com.lethanh98.performance.time;

import com.lethanh98.performance.Main;
import com.lethanh98.performance.tps.TestTpsMultipleService;
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
public class MainTestTpsTimeCountMultiple {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    TestTpsTimeCountMultipleService testTpsMultipleService;
    @Test
    public void testTpsMultiple() throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            testTpsMultipleService.test();
        }
    }
}
