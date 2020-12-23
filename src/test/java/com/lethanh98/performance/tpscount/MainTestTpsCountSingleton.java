package com.lethanh98.performance.tpscount;

import com.lethanh98.performance.Main;
import com.lethanh98.performance.tps.TestTpsSingletonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = Main.class)
@TestPropertySource(properties = {"application.properties"})
public class MainTestTpsCountSingleton {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    TestTpsCountMultipleService singletonService;
    @Test
    public void testTpsCountSingleton() throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            singletonService.test();
        }
        Thread.sleep(11000);
    }
}
