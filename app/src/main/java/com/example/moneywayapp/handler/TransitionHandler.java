package com.example.moneywayapp.handler;

import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

public interface TransitionHandler {
    void moveToProfile();

    void moveToIncome();

    void moveToExpense();

    void moveToHistory();

    void moveToLastFragment();

    void moveToCategory(CategoryDTO category, TypeOperation typeOperation);
}
