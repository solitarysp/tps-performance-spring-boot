package com.lethanh98.performance;

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
public class MainTestTpsSingleton {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    TestTpsSingletonService singletonService;
    @Test
    public void testTpsSingleton() throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            singletonService.test();
        }
        Thread.sleep(11000);
    }
}
