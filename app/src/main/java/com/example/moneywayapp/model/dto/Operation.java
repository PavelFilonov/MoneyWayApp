package com.example.moneywayapp.model.dto;

public class Operation {

    private Long id;
    private TypeOperation type;
    private Category category;
    private User user;
    private Double value;
    private String createdAt;

    public Operation(Long id, TypeOperation type, Category category, User user, Double value, String createdAt) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.user = user;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Operation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
