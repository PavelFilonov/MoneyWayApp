package com.example.moneywayapp.model.dto;

public class OperationDTO {

    private Long id;
    private TypeOperation type;
    private CategoryDTO category;
    private UserDTO user;
    private Double value;
    private String createdAt;

    public OperationDTO(Long id, TypeOperation type, CategoryDTO category, UserDTO user, Double value, String createdAt) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.user = user;
        this.value = value;
        this.createdAt = createdAt;
    }

    public OperationDTO() {
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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
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
