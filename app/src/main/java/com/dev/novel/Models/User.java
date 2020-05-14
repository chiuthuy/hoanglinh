package com.dev.novel.Models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean compare(User other) {
        boolean usernameMatched = other.username.compareTo(this.username) == 0;
        boolean passwordMatched = other.password.compareTo(this.password) == 0;

        return usernameMatched && passwordMatched;
    }

    public Map<String, String> toMap() {
        Map<String, String> userMap = new HashMap<>();

        userMap.put("username", this.username);
        userMap.put("password", this.password);

        return userMap;
    }
}
