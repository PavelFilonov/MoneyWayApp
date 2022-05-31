package com.example.moneywayapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.UserAPI;
import com.example.moneywayapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailText = findViewById(R.id.editTextLoginEmail);
        passwordText = findViewById(R.id.editTextLoginPassword);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registrationTextView = findViewById(R.id.registerTextView);

        loginButton.setOnClickListener(this::onClickLoginButton);
        registrationTextView.setOnClickListener(this::onClickRegistrationTextView);
    }

    private void onClickLoginButton(View view) {
        User user = new User();
        user.setEmail(emailText.getText().toString());
        user.setPassword(passwordText.getText().toString());

        UserAPI userAPI = HelperAPI.getRetrofit().create(UserAPI.class);
        Call<Void> call = userAPI.login(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 404:
                        Log.i(TAG, response.message());
                        Toast.makeText(getApplicationContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
                        break;
                    case 409:
                        Log.i(TAG, response.message());
                        Toast.makeText(getApplicationContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                        break;
                    case 200:
                        Log.i(TAG, response.message());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });
    }

    private void onClickRegistrationTextView(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}