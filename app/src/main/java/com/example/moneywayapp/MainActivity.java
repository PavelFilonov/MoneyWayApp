package com.example.moneywayapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.UserAPI;
import com.example.moneywayapp.model.dto.User;
import com.example.moneywayapp.util.TransitionHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TransitionHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static User user;

    public static UserAPI userAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initUser();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(nav_listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment(this)).commit();
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
                        user = null;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, new IncomeFragment()).commit();
    }

    @Override
    public void moveToExpense() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, new ExpenseFragment()).commit();
    }

    @Override
    public void moveToHistory() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, new HistoryFragment()).commit();
    }

    private void initUser() {
        userAPI = HelperAPI.getRetrofit().create(UserAPI.class);
        Call<User> call = userAPI.profile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                TextView usernameTextView = findViewById(R.id.usernameWalletTextView);
                usernameTextView.setText(user.getLogin());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });
    }
}