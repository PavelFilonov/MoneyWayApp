package com.example.moneywayapp.model.dto;

import java.util.List;

public class UserDTO {

    private Long id;
    private String email;
    private String login;
    private String password;
    private List<GroupDTO> groups;
    private List<CategoryDTO> categories;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String login, String password, List<GroupDTO> groups, List<CategoryDTO> categories) {
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

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
