package com.example.moneywayapp;

import static com.example.moneywayapp.model.dto.TypeOperation.EXPENSE;
import static com.example.moneywayapp.model.dto.TypeOperation.INCOME;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.handler.WalletHandler;
import com.example.moneywayapp.model.TypeWallet;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.GroupDTO;
import com.example.moneywayapp.model.dto.TypeOperation;
import com.example.moneywayapp.model.response.AuthResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements TransitionHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static AuthResponse auth;

    public static String token;

    private Fragment lastWalletFragment;

    private Fragment lastGroupFragment;

    private WalletFragment walletFragment;

    private GroupFragment groupFragment;

    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        walletFragment = new WalletFragment(this);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

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
                        fragment = new GroupsFragment(this);
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
        lastWalletFragment = new SubWalletFragment(INCOME, walletFragment, this, TypeWallet.USER);
        moveToLastWalletFragment();
    }

    @Override
    public void moveToExpense() {
        lastWalletFragment = new SubWalletFragment(EXPENSE, walletFragment, this, TypeWallet.USER);
        moveToLastWalletFragment();
    }

    @Override
    public void moveToHistory() {
        lastWalletFragment = new HistoryFragment(TypeWallet.USER, walletFragment);
        moveToLastWalletFragment();
    }

    @Override
    public void moveToLastWalletFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, walletFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_wallet, lastWalletFragment).commit();
    }

    @Override
    public void moveToCategory(CategoryDTO category, TypeOperation typeOperation, TypeWallet typeWallet) {
        WalletHandler walletHandler = null;

        if (typeWallet.equals(TypeWallet.USER))
            walletHandler = walletFragment;
        else if (typeWallet.equals(TypeWallet.GROUP))
            walletHandler = groupFragment;

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container,
                new CategoryItemFragment(category, this, typeOperation, walletHandler, typeWallet))
                .commit();
    }

    @Override
    public void moveToGroup(GroupDTO group) {
        groupFragment = new GroupFragment(group, this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, groupFragment).commit();
    }

    @Override
    public void moveToGroupIncome() {
        lastGroupFragment = new SubWalletFragment(INCOME, groupFragment, this, TypeWallet.GROUP);
        moveToLastGroupFragment();
    }

    @Override
    public void moveToGroupExpense() {
        lastGroupFragment = new SubWalletFragment(EXPENSE, groupFragment, this, TypeWallet.GROUP);
        moveToLastGroupFragment();
    }

    @Override
    public void moveToGroupHistory() {
        lastGroupFragment = new HistoryFragment(TypeWallet.GROUP, groupFragment);
        moveToLastGroupFragment();
    }

    @Override
    public void moveToLastGroupFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, groupFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_group, lastGroupFragment).commit();
    }

    @Override
    public void moveToLastFragment(TypeWallet typeWallet) {
        if (typeWallet.equals(TypeWallet.USER))
            moveToLastWalletFragment();
        else if (typeWallet.equals(TypeWallet.GROUP))
            moveToLastGroupFragment();
    }

    @Override
    public void moveToGroups() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GroupsFragment(this)).commit();
    }

    @Override
    public void copyText(String text) {
        ClipData clipData = ClipData.newPlainText("token", text);
        clipboardManager.setPrimaryClip(clipData);
    }

    @Override
    public void moveToGroupItem(GroupDTO group) {
        Fragment groupItemFragment = new GroupItemUsersFragment(group, this);
        if (group.getOwnerId().equals(auth.getUser().getId()))
            groupItemFragment = new GroupItemOwnerFragment(group, this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, groupItemFragment).commit();
    }

    public static void joinThread(Runnable task) {
        Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.w(TAG, e.getMessage());
        }
    }
}