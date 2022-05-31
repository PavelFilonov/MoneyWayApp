package com.example.moneywayapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.OperationAPI;
import com.example.moneywayapp.model.Category;
import com.example.moneywayapp.model.Empty;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        toProfileButton = requireView().findViewById(R.id.toProfileButton);
        usernameTextView = requireView().findViewById(R.id.usernameWalletTextView);
        totalMoney = requireView().findViewById(R.id.totalWalletTextView);
        nameNewCategoryText = requireView().findViewById(R.id.editTextWalletNameNewCategory);
        incomeButton = requireView().findViewById(R.id.incomeWalletButton);
        expenseButton = requireView().findViewById(R.id.expenseWalletButton);
        historyButton = requireView().findViewById(R.id.historyWalletButton);
        newCategoryButton = requireView().findViewById(R.id.newCategoryWalletButton);

        incomeButton.setOnClickListener(this::onClickedIncomeButton);
        expenseButton.setOnClickListener(this::onClickedExpenseButton);
        historyButton.setOnClickListener(this::onClickedHistoryButton);
        newCategoryButton.setOnClickListener(this::onClickedNewCategoryButton);

        categoryAPI = HelperAPI.getRetrofit().create(CategoryOfUserAPI.class);
        operationAPI = HelperAPI.getRetrofit().create(OperationAPI.class);

        onClickedIncomeButton(incomeButton);

        return inflater.inflate(R.layout.wallet, container, false);
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
        category.setName(nameNewCategoryText.toString());

        Call<Empty> call = categoryAPI.add(category);
        call.enqueue(new Callback<Empty>() {
            @Override
            public void onResponse(Call<Empty> call, Response<Empty> response) {
                String msg = "Категория " + category.getName() + " добавлена";
                Log.i(TAG, msg);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Empty> call, Throwable t) {
                Log.w(TAG, t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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