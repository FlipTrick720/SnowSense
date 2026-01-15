package com.notification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Fallback controller for SPA routing
 * 
 * Serves index.html for all frontend routes that are not API endpoints.
 * This allows direct access to routes like /app/home, /app/search, etc.
 * without getting 404 errors - a common issue with single page applications.
 */
@Controller
public class SpaFallbackController {

    /**
     * Catch-all handler for SPA routes
     * Handles paths like /app/home, /app/search, /tabs, etc.
     * 
     * Note: Must be the LAST route handler (lowest priority)
     * API routes (@RestController) and static resources are handled first
     */
    @GetMapping("/{x:^(?!api).*}")
    public String forward() {
        return "forward:/index.html";
    }

    /**
     * Handle multi-segment paths like /app/home/details
     */
    @GetMapping("/{x:^(?!api).*}/**")
    public String forwardMultiSegment() {
        return "forward:/index.html";
    }
}



