package com.dev.novel.Models;
import android.util.ArrayMap;

import java.util.Map;

public class Chapter {
    private String id, title, content;

    public Chapter() {}

    public Chapter(
        String id,
        String title,
        String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new ArrayMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("content", content);

        return map;
    }
}
