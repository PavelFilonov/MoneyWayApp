package com.example.moneywayapp.model.dto;

import java.util.List;

public class User {

    private Long id;
    private String email;
    private String login;
    private String password;
    private List<Group> groups;
    private List<Category> categories;

    public User() {
    }

    public User(Long id, String email, String login, String password, List<Group> groups, List<Category> categories) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.password = password;
        this.groups = groups;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
