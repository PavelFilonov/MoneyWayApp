package com.example.moneywayapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.UserAPI;
import com.example.moneywayapp.model.dto.User;
import com.example.moneywayapp.model.response.AuthResponse;
import com.example.moneywayapp.handler.TransitionHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TransitionHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static UserAPI userAPI;

    public static AuthResponse auth;

    public static String token;

    public static Fragment lastFragment;

    private WalletFragment walletFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        userAPI = HelperAPI.getRetrofitAuth().create(UserAPI.class);
        Call<User> profile = userAPI.profile();
        profile.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                auth.setUser(response.body()); // TODO: нужен id
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });

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
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, new ProfileFragment(this)).commit();
    }

    @Override
    public void moveToIncome() {
        lastFragment = new IncomeFragment(walletFragment);
        moveToLastFragment();
    }

    @Override
    public void moveToExpense() {
        lastFragment = new ExpenseFragment();
        moveToLastFragment();
    }

    @Override
    public void moveToHistory() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, new HistoryFragment()).commit();
    }

    @Override
    public void moveToLastFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, lastFragment).commit();
    }
}