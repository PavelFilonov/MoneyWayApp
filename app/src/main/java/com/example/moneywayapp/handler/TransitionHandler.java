package com.example.moneywayapp.handler;

public interface TransitionHandler {
    void moveToProfile();

    void moveToIncome();

    void moveToExpense();

    void moveToHistory();

    void moveToLastFragment();
}
