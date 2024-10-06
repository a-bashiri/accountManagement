package com.example.accountmanagement.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenStore {
    private final Map<String, String> userTokens = new HashMap<>();

    public String getActiveToken(String userId) {
        return userTokens.get(userId);
    }

    public void storeToken(String userId, String token) {
        userTokens.put(userId, token);
    }

    public void removeToken(String userId) {
        userTokens.remove(userId);
    }
}
