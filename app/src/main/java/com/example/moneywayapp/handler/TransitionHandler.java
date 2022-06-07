package com.example.moneywayapp.handler;

import com.example.moneywayapp.model.TypeWallet;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.GroupDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

public interface TransitionHandler {
    void moveToProfile();

    void moveToIncome();

    void moveToExpense();

    void moveToHistory();

    void moveToLastWalletFragment();

    void moveToCategory(CategoryDTO category, TypeOperation typeOperation, TypeWallet typeWallet);

    void moveToGroup(GroupDTO group);

    void moveToGroupIncome();

    void moveToGroupExpense();

    void moveToGroupHistory();

    void moveToLastGroupFragment();

    void moveToLastFragment(TypeWallet typeWallet);

    void moveToGroups();

    void copyText(String text);

    void moveToGroupItem(GroupDTO group);
}
