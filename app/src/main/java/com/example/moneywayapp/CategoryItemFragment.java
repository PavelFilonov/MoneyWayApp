package com.example.moneywayapp;

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

import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.OperationAPI;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.OperationDTO;
import com.example.moneywayapp.model.dto.TypeOperation;
import com.example.moneywayapp.handler.CategoryHandler;
import com.example.moneywayapp.handler.TransitionHandler;

import java.time.LocalDateTime;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryItemFragment extends Fragment {

    private static final String TAG = CategoryItemFragment.class.getSimpleName();

    private TextView nameCategoryItem;

    private EditText newOperationValue, newNameCategoryText;

    private final CategoryDTO category;

    private final TransitionHandler transitionHandler;

    private final CategoryHandler categoryHandler;

    private OperationAPI operationAPI;

    private final TypeOperation typeOperation;

    public CategoryItemFragment(CategoryDTO category, TransitionHandler transitionHandler,
                                TypeOperation typeOperation, CategoryHandler categoryHandler) {
        super(R.layout.category_item);
        this.category = category;
        this.transitionHandler = transitionHandler;
        this.typeOperation = typeOperation;
        this.categoryHandler = categoryHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backButton = requireView().findViewById(R.id.backFromCategoryButton);
        ImageButton deleteButton = requireView().findViewById(R.id.deleteCategoryButton);
        nameCategoryItem = requireView().findViewById(R.id.nameCategoryItemText);
        newOperationValue = requireView().findViewById(R.id.editTextNewOperation);
        Button addOperationButton = requireView().findViewById(R.id.addOperationButton);
        newNameCategoryText = requireView().findViewById(R.id.editTextNewNameCategory);
        Button renameCategoryButton = requireView().findViewById(R.id.renameCategoryButton);

        operationAPI = HelperAPI.getRetrofitAuth().create(OperationAPI.class);
        nameCategoryItem.setText(category.getName());

        backButton.setOnClickListener(this::onClickedBackButton);
        deleteButton.setOnClickListener(this::onClickedDeleteButton);
        addOperationButton.setOnClickListener(this::onClickedAddOperationButton);
        renameCategoryButton.setOnClickListener(this::onClickedRenameCategoryButton);
    }

    private void onClickedRenameCategoryButton(View view) {
        String name = newNameCategoryText.getText().toString();
        categoryHandler.rename(category.getId(), name);
        nameCategoryItem.setText(name);
    }

    private void onClickedAddOperationButton(View view) {
        OperationDTO operation = new OperationDTO();
        operation.setCategory(category);
        operation.setType(typeOperation);
        operation.setCreatedAt(LocalDateTime.now().toString());

        try {
            operation.setValue(Double.parseDouble(newOperationValue.getText().toString()));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Неправильное значение операции", Toast.LENGTH_LONG).show();
            return;
        }

        Call<Void> add = operationAPI.add(operation);
        add.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    Toast.makeText(getContext(), "Операция добавлена", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });
    }

    private void onClickedDeleteButton(View view) {
        categoryHandler.deleteCategory(category, typeOperation);
        transitionHandler.moveToLastFragment();
    }

    private void onClickedBackButton(View view) {
        transitionHandler.moveToLastFragment();
    }
}
