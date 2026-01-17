package com.notification.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConditionsController.class)
class ConditionsControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllConditions_shouldReturnNotImplementedPayload() throws Exception {
        mockMvc.perform(get("/api/conditions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("not_implemented"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    void getConditionsForResort_shouldIncludeResortIdInMessage() throws Exception {
        mockMvc.perform(get("/api/conditions/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("not_implemented"))
                .andExpect(jsonPath("$.message").value("Combined conditions endpoint for resort 123"));
    }

    @Test
    void triggerScrape_shouldReturnNotImplementedPayload() throws Exception {
        mockMvc.perform(post("/api/conditions/scrape"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("not_implemented"))
                .andExpect(jsonPath("$.note").exists());
    }
}
