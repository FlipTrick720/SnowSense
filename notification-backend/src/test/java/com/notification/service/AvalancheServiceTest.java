package com.notification.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AvalancheServiceTest {

    @Autowired
    private AvalancheService avalancheService;

    @Test
    public void testScrapeAvalancheData() {
        avalancheService.manualScrape();
    }
}
