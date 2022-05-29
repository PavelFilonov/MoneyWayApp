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
import com.example.moneywayapp.model.Empty;
import com.example.moneywayapp.model.User;

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
        if (!passwordText1.toString().equals(passwordText2.toString())) {
            Log.w(TAG, "Пароли не совпадают: " + passwordText1.toString() + " и " + passwordText2.toString());
            Toast.makeText(getApplicationContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
        }

        User user = new User();
        user.setEmail(emailText.toString());
        user.setLogin(loginText.toString());
        user.setPassword(passwordText1.toString());

        UserAPI userAPI = HelperAPI.getRetrofit().create(UserAPI.class);
        Call<Empty> call = userAPI.register(user);
        call.enqueue(new Callback<Empty>() {
            @Override
            public void onResponse(Call<Empty> call, Response<Empty> response) {
                Log.i(TAG, response.message());
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                RegistrationActivity.this.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Empty> call, Throwable t) {
                Log.w(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void onClickLoginTextView(View view) {
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        RegistrationActivity.this.startActivity(intent);
    }
}