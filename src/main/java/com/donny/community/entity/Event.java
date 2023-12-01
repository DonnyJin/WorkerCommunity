package com.donny.community.entity;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
public class Event {

    private String topic;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer entityUserId;
    @Setter(AccessLevel.NONE)
    private Map<String, Object> data = new HashMap<>();

    public void setData(String key, Object value) {
        if (key == null) throw new IllegalArgumentException("参数不能为空!");
        data.put(key, value);
    }
}
