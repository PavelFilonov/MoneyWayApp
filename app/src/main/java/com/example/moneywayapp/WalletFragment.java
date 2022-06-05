package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.handler.WalletHandler;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

public class WalletFragment extends Fragment implements WalletHandler {

    private TextView totalMoneyText;

    private Button incomeButton, expenseButton, historyButton;

    private final TransitionHandler transitionHandler;

    public WalletFragment(TransitionHandler transitionHandler) {
        super(R.layout.wallet);
        this.transitionHandler = transitionHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton toProfileButton = requireView().findViewById(R.id.toProfileButton);
        incomeButton = requireView().findViewById(R.id.incomeWalletButton);
        expenseButton = requireView().findViewById(R.id.expenseWalletButton);
        historyButton = requireView().findViewById(R.id.historyWalletButton);
        TextView usernameText = requireView().findViewById(R.id.usernameWalletTextView);
        totalMoneyText = requireView().findViewById(R.id.totalWalletTextView);

        usernameText.setText(auth.getUser().getLogin());

        toProfileButton.setOnClickListener(this::onClickedToProfileButton);
        incomeButton.setOnClickListener(this::onClickedIncomeButton);
        expenseButton.setOnClickListener(this::onClickedExpenseButton);
        historyButton.setOnClickListener(this::onClickedHistoryButton);

        onClickedIncomeButton(incomeButton);
    }

    private void onClickedToProfileButton(View view) {
        transitionHandler.moveToProfile();
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedIncomeButton(View view) {
        doInactiveButtons();
        incomeButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        incomeButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

        transitionHandler.moveToIncome();
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedExpenseButton(View view) {
        doInactiveButtons();
        expenseButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        expenseButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

        transitionHandler.moveToExpense();
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedHistoryButton(View view) {
        doInactiveButtons();
        historyButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        historyButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

        transitionHandler.moveToHistory();
    }

    @SuppressLint("ResourceAsColor")
    private void doInactiveButtons() {
        incomeButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        incomeButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.blue));

        expenseButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        expenseButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.blue));

        historyButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        historyButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.blue));
    }

    @Override
    public void setTotalMoney(String s) {
        totalMoneyText.setText(s);
    }
}