package com.musala.services.booking.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class DeserializerUtil {
    public static String TryStringGetValue(String key, JsonNode node, String defaultValue) {
        JsonNode value = node.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        return value.asText();
    }

    public static int TryIntGetValue(String key, JsonNode node, int defaultValue) {
        JsonNode value = node.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        return value.asInt();
    }

    public static long TryLongGetValue(String key, JsonNode node, long defaultValue) {
        JsonNode value = node.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        return value.asLong();
    }

    public static boolean TryBooleanGetValue(String key, JsonNode node, boolean defaultValue) {
        JsonNode value = node.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        return value.asBoolean();
    }

    public static double TryDoubleGetValue(String key, JsonNode node, double defaultValue) {
        JsonNode value = node.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        return value.asDouble();
    }

    public static float TryFloatGetValue(String key, JsonNode node, float defaultValue) {
        JsonNode value = node.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        return value.floatValue();
    }    
}