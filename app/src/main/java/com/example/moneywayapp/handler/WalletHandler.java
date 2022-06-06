package com.example.moneywayapp.handler;

import com.example.moneywayapp.model.TypeWallet;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

import java.util.List;

public interface WalletHandler {
    void setTotalMoney(String s);

    List<CategoryDTO> getCategories();

    void deleteCategory(CategoryDTO category, TypeOperation typeOperation, TypeWallet typeWallet);

    void renameCategory(Long id, String name);

    void addCategory(CategoryDTO category);
}
