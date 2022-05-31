package com.example.moneywayapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.OperationAPI;
import com.example.moneywayapp.model.Category;
import com.example.moneywayapp.util.TransitionHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {

    private static final String TAG = WalletFragment.class.getSimpleName();

    private ImageButton toProfileButton;

    private TextView usernameTextView, totalMoney;

    private EditText nameNewCategoryText;

    private Button incomeButton, expenseButton, historyButton, newCategoryButton;

    private CategoryOfUserAPI categoryAPI;

    private OperationAPI operationAPI;

    private TransitionHandler transitionHandler;

    public WalletFragment(TransitionHandler transitionHandler) {
        super(R.layout.wallet);
        this.transitionHandler = transitionHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toProfileButton = requireView().findViewById(R.id.toProfileButton);
        usernameTextView = requireView().findViewById(R.id.usernameWalletTextView);
        totalMoney = requireView().findViewById(R.id.totalWalletTextView);
        nameNewCategoryText = requireView().findViewById(R.id.editTextWalletNameNewCategory);
        incomeButton = requireView().findViewById(R.id.incomeWalletButton);
        expenseButton = requireView().findViewById(R.id.expenseWalletButton);
        historyButton = requireView().findViewById(R.id.historyWalletButton);
        newCategoryButton = requireView().findViewById(R.id.newCategoryWalletButton);

        toProfileButton.setOnClickListener(this::onClickedToProfileButton);
        incomeButton.setOnClickListener(this::onClickedIncomeButton);
        expenseButton.setOnClickListener(this::onClickedExpenseButton);
        historyButton.setOnClickListener(this::onClickedHistoryButton);
        newCategoryButton.setOnClickListener(this::onClickedNewCategoryButton);

        categoryAPI = HelperAPI.getRetrofit().create(CategoryOfUserAPI.class);
        operationAPI = HelperAPI.getRetrofit().create(OperationAPI.class);

        onClickedIncomeButton(incomeButton);
    }

    private void onClickedToProfileButton(View view) {
        transitionHandler.moveToProfile();
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedIncomeButton(View view) {
        doInactiveButtons();
        incomeButton.setBackgroundColor(R.color.blue);
        incomeButton.setTextColor(R.color.white);

        // TODO: обработка времени ...
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedExpenseButton(View view) {
        doInactiveButtons();
        expenseButton.setBackgroundColor(R.color.blue);
        expenseButton.setTextColor(R.color.white);

    }

    @SuppressLint("ResourceAsColor")
    private void onClickedHistoryButton(View view) {
        doInactiveButtons();
        historyButton.setBackgroundColor(R.color.blue);
        historyButton.setTextColor(R.color.white);

    }

    private void onClickedNewCategoryButton(View view) {
        Category category = new Category();
        category.setName(nameNewCategoryText.getText().toString());

        Call<Void> call = categoryAPI.add(category);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String msg = "Категория " + category.getName() + " добавлена";

                switch (response.code()) {
                    case 422:
                        Log.i(TAG, response.message());
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        break;
                    case 200:
                        Log.i(TAG, msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });

        // TODO: обновить фрагмент
    }

    @SuppressLint("ResourceAsColor")
    private void doInactiveButtons() {
        incomeButton.setBackgroundColor(R.color.light_gray);
        incomeButton.setTextColor(R.color.blue);

        expenseButton.setBackgroundColor(R.color.light_gray);
        expenseButton.setTextColor(R.color.blue);

        historyButton.setBackgroundColor(R.color.light_gray);
        historyButton.setTextColor(R.color.blue);
    }
}