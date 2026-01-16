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
        scrapingService.triggerScraping(); // Trigger scraping on startup
    }
}
