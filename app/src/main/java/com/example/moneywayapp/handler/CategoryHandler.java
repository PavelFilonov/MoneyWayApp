package com.example.moneywayapp.handler;

import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

public interface CategoryHandler {
    void deleteCategory(CategoryDTO category, TypeOperation typeOperation);

    void rename(Long id, String name);
}
