package com.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.notification.service.ScrapingService;

@SpringBootApplication
public class NotificationApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NotificationApplication.class, args);
        
        ScrapingService scrapingService = context.getBean(ScrapingService.class);
        
        // Run in background so Spring Boot starts immediately and exposes Port 7860
        new Thread(() -> {
            try {
                System.out.println("Server started. Waiting 5s before starting background scraping...");
                Thread.sleep(5000); // Wait 5s for server to settle
                
                System.out.println("Starting Scraping:");
                System.out.println("Port 7860 is exported (For Hugging Face).");
                
                // TRIGGER FULL SCRAPING (Avalanche + Weather + Ski Resorts)
                scrapingService.triggerScraping();
                
            } catch (Exception e) {
                System.err.println("Error in background scraping thread:");
                e.printStackTrace();
            }
        }).start();
    }
}