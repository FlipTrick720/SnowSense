package com.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.notification.service.ScrapingService;

@SpringBootApplication
public class NotificationApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NotificationApplication.class, args);
        
        // Trigger scraping asynchronously so Spring Boot can start the HTTP server
        // and respond to health checks while scraping happens in background
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
