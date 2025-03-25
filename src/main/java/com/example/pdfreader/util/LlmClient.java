package com.example.pdfreader.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LlmClient {

    public static String callLlmApi(String prompt, String apiKey, String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode payloadNode = mapper.createObjectNode();
            payloadNode.put("model", "gpt-3.5-turbo");

            ArrayNode messagesArray = mapper.createArrayNode();
            ObjectNode messageNode = mapper.createObjectNode();
            messageNode.put("role", "user");
            messageNode.put("content", prompt);
            messagesArray.add(messageNode);
            payloadNode.set("messages", messagesArray);

            String payload = mapper.writeValueAsString(payloadNode);
            System.out.println("Payload: " + payload);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (connection.getResponseCode() >= 400) {
                InputStream errorStream = connection.getErrorStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = errorStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                String errorResponse = new String(result.toByteArray(), "UTF-8");
                System.err.println("Error Response from API: " + errorResponse);
                throw new RuntimeException("LLM API Error: " + errorResponse);
            }

            JsonNode responseJson = mapper.readTree(connection.getInputStream());
            String content = responseJson.get("choices").get(0).get("message").get("content").asText();
            return content;
        } catch (Exception e) {
            throw new RuntimeException("Error calling LLM API: " + e.getMessage(), e);
        }
    }
}
