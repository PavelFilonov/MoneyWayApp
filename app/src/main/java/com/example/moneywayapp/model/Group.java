package com.example.moneywayapp.model;

import java.util.List;

public class Group {

    private Long id;
    private String name;
    private String token;
    private Long ownerId;
    private List<Category> categories;

    public Group(Long id, String name, String token, Long ownerId, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.ownerId = ownerId;
        this.categories = categories;
    }

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
