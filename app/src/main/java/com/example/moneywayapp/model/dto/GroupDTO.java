package com.example.moneywayapp.model.dto;

import java.util.List;

public class GroupDTO {

    private Long id;
    private String name;
    private String token;
    private Long ownerId;
    private List<CategoryDTO> categories;

    public GroupDTO(Long id, String name, String token, Long ownerId, List<CategoryDTO> categories) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.ownerId = ownerId;
        this.categories = categories;
    }

    public GroupDTO() {
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

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
