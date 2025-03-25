package com.example.pdfreader.model;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PdfDataResponse {
    private String name;
    private String email;
    private String openingBalance;
    private String closingBalance;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(String openingBalance) { this.openingBalance = openingBalance; }

    public String getClosingBalance() { return closingBalance; }
    public void setClosingBalance(String closingBalance) { this.closingBalance = closingBalance; }
    
    /**
     * Converts a JSON string to a PdfDataResponse object.
     */
    public static PdfDataResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, PdfDataResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response: " + e.getMessage(), e);
        }
    }
}
