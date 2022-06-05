package com.example.moneywayapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.handler.CategoryHandler;
import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.TypeOperation;
import com.example.moneywayapp.model.response.AuthResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements TransitionHandler, CategoryHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static AuthResponse auth;

    public static String token;

    public static Fragment lastFragment;

    private WalletFragment walletFragment;

    private CategoryOfUserAPI categoryOfUserAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        categoryOfUserAPI = HelperAPI.getRetrofitAuth().create(CategoryOfUserAPI.class);
        walletFragment = new WalletFragment(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav_listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, walletFragment).commit();
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener nav_listener =
            item -> {
                Fragment fragment = null;
                boolean exit = false;

                switch (item.getItemId()) {
                    case R.id.menu_wallet:
                        fragment = new WalletFragment(this);
                        break;
                    case R.id.menu_groups:
                        fragment = new GroupsFragment();
                        break;
                    case R.id.menu_exit:
                        auth = null;
                        exit = true;
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent);
                        break;
                }
                if (!exit)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            };

    @Override
    public void moveToProfile() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment(this)).commit();
    }

    @Override
    public void moveToIncome() {
        lastFragment = new IncomeFragment(walletFragment, this);
        moveToLastFragment();
    }

    @Override
    public void moveToExpense() {
        lastFragment = new ExpenseFragment();
        moveToLastFragment();
    }

    @Override
    public void moveToHistory() {
        lastFragment = new HistoryFragment();
        moveToLastFragment();
    }

    @Override
    public void moveToLastFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, walletFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, lastFragment).commit();
    }

    @Override
    public void moveToCategory(CategoryDTO category, TypeOperation typeOperation) {
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                new CategoryItemFragment(category, this, typeOperation, this))
                .commit();
    }

    @Override
    public void deleteCategory(CategoryDTO category, TypeOperation typeOperation) {
        Runnable task = () -> {
            Call<Void> delete = categoryOfUserAPI.delete(category.getId());
            try {
                delete.execute();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);

        if (typeOperation.equals(TypeOperation.INCOME))
            lastFragment = new IncomeFragment(walletFragment, this);
        else if (typeOperation.equals(TypeOperation.EXPENSE))
            lastFragment = new ExpenseFragment();
    }

    @Override
    public void rename(Long id, String name) {
        Runnable task = () -> {
            Call<Void> rename = categoryOfUserAPI.rename(id, name);
            try {
                rename.execute();
                Log.i(TAG, "Категория переименована");
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);
    }

    private void joinThread(Runnable task) {
        Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.w(TAG, e.getMessage());
        }
    }
}