package com.reactive.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.text.MessageFormat.format;

@Slf4j
@Data
public final class JsonUtils {

    private static final ObjectMapper objectMapper;
    private static final String FAIL_TO_SERIALIZE = "Fail to serialize the request. {0}";
    private static final String FAIL_TO_DESERIALIZE = "Fail to deserialize the request. {0}";

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException ioe) {
            log.error("IOException converting to json", ioe);
            throw new RuntimeException(format(FAIL_TO_SERIALIZE, ioe.getMessage()));
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json.replaceAll("\u200B", ""), clazz);
        } catch (IOException ioe) {
            log.error("IOException converting from json", ioe);
            throw new RuntimeException(format(FAIL_TO_DESERIALIZE, ioe.getMessage()));
        }
    }
}
