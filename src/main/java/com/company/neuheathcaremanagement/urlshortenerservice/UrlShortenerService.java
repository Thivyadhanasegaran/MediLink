package com.company.neuheathcaremanagement.urlshortenerservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UrlShortenerService {

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    @Value("${bitly.access.token}")
    private String accessToken;
    

    private static final String BITLY_API_URL = "https://api-ssl.bitly.com/v4/shorten";

    public String shortenUrl(String longUrl) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        String requestBody = "{\"long_url\": \"" + longUrl + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(BITLY_API_URL, HttpMethod.POST, requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            return extractShortUrl(responseBody);
        } catch (Exception e) {
            logger.error("Error occurred while shortening URL: {}", longUrl, e);
            throw new RuntimeException("Failed to shorten URL", e);
        }
    }

    private String extractShortUrl(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(responseBody);
            JsonNode linkNode = jsonNode.path("link");
            if (linkNode.isMissingNode()) {
                logger.error("Short URL not found in Bitly response: {}", responseBody);
                throw new RuntimeException("Short URL not found in Bitly response");
            }
            return linkNode.asText();
        } catch (Exception e) {
            logger.error("Failed to parse short URL from Bitly response: {}", responseBody, e);
            throw new RuntimeException("Failed to parse short URL from Bitly response", e);
        }
    }
}
