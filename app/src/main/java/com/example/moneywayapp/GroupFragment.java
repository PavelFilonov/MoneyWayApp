package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.joinThread;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfGroupAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.handler.WalletHandler;
import com.example.moneywayapp.model.TypeWallet;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.GroupDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GroupFragment extends Fragment implements WalletHandler {

    private static final String TAG = GroupFragment.class.getSimpleName();

    private final GroupDTO group;

    private TextView totalGroup;

    private Button incomeButton, expenseButton, historyButton;

    private final TransitionHandler transitionHandler;

    private CategoryOfGroupAPI categoryAPI;

    public GroupFragment(GroupDTO group, TransitionHandler transitionHandler) {
        super(R.layout.group);
        this.group = group;
        this.transitionHandler = transitionHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameGroup = requireView().findViewById(R.id.nameGroupTextView);
        totalGroup = requireView().findViewById(R.id.totalGroupTextView);
        incomeButton = requireView().findViewById(R.id.incomeGroupButton);
        expenseButton = requireView().findViewById(R.id.expenseGroupButton);
        historyButton = requireView().findViewById(R.id.historyGroupButton);

        categoryAPI = HelperAPI.getRetrofitAuth().create(CategoryOfGroupAPI.class);
        nameGroup.setText(group.getName());

        incomeButton.setOnClickListener(this::onClickedIncomeButton);
        expenseButton.setOnClickListener(this::onClickedExpenseButton);
        historyButton.setOnClickListener(this::onClickedHistoryButton);

        onClickedIncomeButton(incomeButton);
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedIncomeButton(View view) {
        doInactiveButtons();
        incomeButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        incomeButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

        transitionHandler.moveToGroupIncome();
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedExpenseButton(View view) {
        doInactiveButtons();
        expenseButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        expenseButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

        transitionHandler.moveToGroupExpense();
    }

    @SuppressLint("ResourceAsColor")
    private void onClickedHistoryButton(View view) {
        doInactiveButtons();
        historyButton.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue));
        historyButton.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white));

        transitionHandler.moveToGroupHistory();
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
        totalGroup.setText(s);
    }

    List<CategoryDTO> categories;

    @Override
    public List<CategoryDTO> getCategories() {
        Runnable task = () -> {
            Call<List<CategoryDTO>> call = categoryAPI.get(group.getId());
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
            Call<Void> delete = categoryAPI.delete(category.getId(), group.getId());
            try {
                Response<Void> execute = delete.execute();
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
            Call<Void> rename = categoryAPI.rename(id, group.getId(), name);
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
            Call<Void> add = categoryAPI.add(category, group.getId());
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