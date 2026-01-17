package com.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.notification.service.ScrapingService;

import com.notification.service.impl.ScrapingServiceImpl; // Import implementation if needed for casting

@SpringBootApplication
public class NotificationApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NotificationApplication.class, args);
        
        ScrapingService scrapingService = context.getBean(ScrapingService.class);
        
        // Run in background so Spring Boot starts immediately
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Wait 5s for server to settle
                
                // USE FAST SCRAPING (Avalanche + Weather only)
                // This prevents crashing the container due to memory/cpu usage
                if (scrapingService instanceof ScrapingServiceImpl) {
                    ((ScrapingServiceImpl) scrapingService).triggerFastStartupScraping();
                } else {
                    scrapingService.triggerScraping();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
