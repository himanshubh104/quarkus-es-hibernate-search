package com.hb.utility;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectHelper {
//    public static final ObjectMapper mapper;
//
//    static {
//        mapper = JsonMapper.builder()
//                .addModule(new BlackbirdModule())
//                .build();
//    }

    public static <T> String convertToJSONString(T value) {
//        try {
//            return mapper.writeValueAsString(value);
//        } catch (JsonProcessingException e) {
//            log.error("Conversion Fail: {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
        return JSON.toJSONString(value);
    }

    public static <T> T parseToObject(String value, Class<T> clazz) {
//        try {
//            return mapper.readValue(value, clazz);
//        } catch (JsonProcessingException e) {
//            log.error("Conversion Fail: {}", e.getMessage());
//            throw new RuntimeException(e);
//        }
        return JSON.parseObject(value, clazz);
    }

    public static <R, T> T convertValue(R value, Class<T> clazz) {
        return JSON.to(clazz, value);
    }
}
