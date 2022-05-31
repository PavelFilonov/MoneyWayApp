package com.example.moneywayapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.UserAPI;
import com.example.moneywayapp.util.TransitionHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements TransitionHandler {

    public static TransitionHandler transitionHandler = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
                        UserAPI userAPI = HelperAPI.getRetrofit().create(UserAPI.class);
                        userAPI.logout();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
    }
}