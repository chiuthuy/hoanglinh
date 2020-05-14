package com.dev.novel.Models;

public class Novel {
    private String id, name;

    public Novel() {}

    public Novel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
