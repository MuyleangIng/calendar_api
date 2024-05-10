package com.example.idata.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SchemaParser {
    public static Map<String, Object> parseSchema(String content) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, Map.class);
    }
}
