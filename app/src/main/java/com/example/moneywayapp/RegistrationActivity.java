package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.authResponse;
import static com.example.moneywayapp.MainActivity.token;

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
import com.example.moneywayapp.model.dto.User;
import com.example.moneywayapp.model.response.AuthResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private EditText emailText, loginText, passwordText1, passwordText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        emailText = findViewById(R.id.editTextRegistrationEmail);
        loginText = findViewById(R.id.editTextRegistrationUsername);
        passwordText1 = findViewById(R.id.editTextRegistrationPassword1);
        passwordText2 = findViewById(R.id.editTextRegistrationPassword2);
        Button registrationButton = findViewById(R.id.registrationButton);
        TextView loginTextView = findViewById(R.id.loginTextView);

        registrationButton.setOnClickListener(this::onClickRegistrationButton);
        loginTextView.setOnClickListener(this::onClickLoginTextView);
    }

    private void onClickRegistrationButton(View view) {
        if (!passwordText1.getText().toString().equals(passwordText2.getText().toString())) {
            Log.w(TAG, "Пароли не совпадают: " + passwordText1.getText() + " и " + passwordText2.getText());
            Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
        }

        User user = new User();
        user.setEmail(emailText.getText().toString());
        user.setLogin(loginText.getText().toString());
        user.setPassword(passwordText1.getText().toString());

        UserAPI userAPI = HelperAPI.getRetrofit().create(UserAPI.class);
        Call<AuthResponse> call = userAPI.register(user);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                switch (response.code()) {
                    case 422:
                        Log.i(TAG, response.message());
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        break;
                    case 409:
                        Log.i(TAG, response.message());
                        Toast.makeText(getApplicationContext(), "Пользователь уже существует", Toast.LENGTH_SHORT).show();
                        break;
                    case 201:
                        authResponse = response.body();
                        token = "Bearer_" + authResponse.getToken();
                        Log.i(TAG, response.message());
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        RegistrationActivity.this.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });

    }

    private void onClickLoginTextView(View view) {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        RegistrationActivity.this.startActivity(intent);
    }
}