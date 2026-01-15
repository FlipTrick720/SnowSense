package com.notification.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Fallback controller for SPA routing
 * 
 * Serves index.html for all frontend routes that are not API endpoints.
 * This allows direct access to routes like /app/home, /app/search, etc.
 * without getting 404 errors - a common issue with single page applications.
 * 
 * The regex pattern ^(?!api) excludes /api/* routes so they're handled by REST controllers.
 */
@Controller
public class SpaFallbackController {

    /**
     * Catch-all for non-API routes
     * Forwards to / which Spring resolves to index.html from static resources
     * 
     * Excludes:
     * - /api/* (REST API endpoints)
     * - Static resources (handled by Spring's default resource handling)
     */
    @GetMapping(value = "/**/{path:^(?!api|[.]*).*$}")
    public String forwardToIndexHtml() {
        return "forward:/index.html";
    }
}

