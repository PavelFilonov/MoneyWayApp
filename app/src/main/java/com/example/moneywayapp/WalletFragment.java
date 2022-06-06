package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.auth;
import static com.example.moneywayapp.MainActivity.joinThread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.handler.WalletHandler;
import com.example.moneywayapp.model.TypeWallet;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class WalletFragment extends Fragment implements WalletHandler {

    private static final String TAG = WalletFragment.class.getSimpleName();

    private TextView totalMoneyText;

    private Button incomeButton, expenseButton, historyButton;

    private final TransitionHandler transitionHandler;

    private CategoryOfUserAPI categoryAPI;

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

        categoryAPI = HelperAPI.getRetrofitAuth().create(CategoryOfUserAPI.class);
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

    List<CategoryDTO> categories;

    @Override
    public List<CategoryDTO> getCategories() {
        Runnable task = () -> {
            Call<List<CategoryDTO>> call = categoryAPI.get();
            Response<List<CategoryDTO>> response;
            try {
                response = call.execute();
                categories = response.body();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);

        if (categories == null)
            return new ArrayList<>();

        return categories;
    }

    @Override
    public void deleteCategory(CategoryDTO category, TypeOperation typeOperation, TypeWallet typeWallet) {
        Runnable task = () -> {
            Call<Void> delete = categoryAPI.delete(category.getId());
            try {
                delete.execute();
                Log.i(TAG, "Категория удалена");
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);
    }

    @Override
    public void renameCategory(Long id, String name) {
        Runnable task = () -> {
            Call<Void> rename = categoryAPI.rename(id, name);
            try {
                rename.execute();
                Log.i(TAG, "Категория переименована");
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);
    }

    @Override
    public void addCategory(CategoryDTO category) {
        Runnable task = () -> {
            Call<Void> add = categoryAPI.add(category);
            try {
                add.execute();
                Log.i(TAG, "Категория " + category.getName() + " добавлена");
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);
    }
}