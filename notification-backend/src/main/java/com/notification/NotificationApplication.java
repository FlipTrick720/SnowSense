package com.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.notification.service.ScrapingService;

@SpringBootApplication
public class NotificationApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NotificationApplication.class, args);
        
        // Trigger FAST startup scraping asynchronously in background
        // Only avalanche and weather data (~2 minutes)
        // This allows Spring Boot to start the HTTP server immediately
        // and respond to health checks within seconds
        // Ski resort data is available from database and can be updated manually via /api/scrape
        ScrapingService scrapingService = context.getBean(ScrapingService.class);
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Wait 2 seconds for server to fully initialize
                scrapingService.triggerScraping();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
