package com.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    @JsonProperty("error")
    private String error;
    
    @JsonProperty("details")
    private List<String> details;
    
    public ErrorResponse(String error) {
        this.error = error;
        this.details = List.of();
    }
}
