package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.user;

import android.annotation.SuppressLint;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.OperationAPI;
import com.example.moneywayapp.model.Category;
import com.example.moneywayapp.util.TransitionHandler;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {

    private static final String TAG = WalletFragment.class.getSimpleName();

    private TextView totalMoney;

    private EditText nameNewCategoryText;

    private Button incomeButton, expenseButton, historyButton;

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

        ImageButton toProfileButton = requireView().findViewById(R.id.toProfileButton);
        totalMoney = requireView().findViewById(R.id.totalWalletTextView);
        nameNewCategoryText = requireView().findViewById(R.id.editTextWalletNameNewCategory);
        incomeButton = requireView().findViewById(R.id.incomeWalletButton);
        expenseButton = requireView().findViewById(R.id.expenseWalletButton);
        historyButton = requireView().findViewById(R.id.historyWalletButton);
        Button newCategoryButton = requireView().findViewById(R.id.newCategoryWalletButton);

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
        incomeButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        incomeButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

        // TODO: обработка времени ...
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedExpenseButton(View view) {
        doInactiveButtons();
        expenseButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        expenseButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

    }

    @SuppressLint("ResourceAsColor")
    private void onClickedHistoryButton(View view) {
        doInactiveButtons();
        historyButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        historyButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

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
        incomeButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        incomeButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.blue));

        expenseButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        expenseButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.blue));

        historyButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
        historyButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.blue));
    }
}