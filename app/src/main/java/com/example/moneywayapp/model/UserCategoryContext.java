package com.example.moneywayapp.model;

public class UserCategoryContext {
    private User user;
    private Category category;

    public UserCategoryContext(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    public UserCategoryContext() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
