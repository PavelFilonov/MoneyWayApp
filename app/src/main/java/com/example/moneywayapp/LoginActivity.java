package com.example.moneywayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneywayapp.api.UserAPI;
import com.example.moneywayapp.model.Empty;
import com.example.moneywayapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://moneywayapi.herokuapp.com/";
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
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserAPI userAPI = retrofit.create(UserAPI.class);

        User user = new User();
        user.setEmail(emailText.toString());
        user.setPassword(passwordText.toString());

        Call<Empty> call = userAPI.login(user);
        call.enqueue(new Callback<Empty>() {
            @Override
            public void onResponse(Call<Empty> call, Response<Empty> response) {
                Log.w(TAG, response.message());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Empty> call, Throwable t) {
                Log.w(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onClickRegistrationTextView(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }
}