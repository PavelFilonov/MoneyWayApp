package com.example.moneywayapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.UserAPI;
import com.example.moneywayapp.model.Empty;
import com.example.moneywayapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private EditText emailText, loginText, passwordText;

    private User user;

    private UserAPI userAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        TextView loginTextView = requireView().findViewById(R.id.usernameProfileTextView);
        emailText = requireView().findViewById(R.id.editTextProfileEmail);
        loginText = requireView().findViewById(R.id.editTextProfileUsername);
        passwordText = requireView().findViewById(R.id.editTextProfilePassword);
        ImageButton updateEmailButton = requireView().findViewById(R.id.updateEmailButton);
        ImageButton updateLoginButton = requireView().findViewById(R.id.updateLoginButton);
        ImageButton updatePasswordButton = requireView().findViewById(R.id.updatePasswordButton);

        initUser();

        loginTextView.setText(user.getLogin());

        updateEmailButton.setOnClickListener(this::onClickedUpdateEmailButton);
        updateLoginButton.setOnClickListener(this::onClickedUpdateLoginButton);
        updatePasswordButton.setOnClickListener(this::onClickedUpdatePasswordButton);

        return inflater.inflate(R.layout.profile, container, false);
    }

    private void onClickedUpdateEmailButton(View view) {
        Call<Empty> call = userAPI.updateEmail(emailText.toString());
        callEnqueue(call, "Email изменён");
    }

    private void onClickedUpdateLoginButton(View view) {
        Call<Empty> call = userAPI.updateLogin(loginText.toString());
        callEnqueue(call, "Логин изменён");
    }

    private void onClickedUpdatePasswordButton(View view) {
        Call<Empty> call = userAPI.updatePassword(passwordText.toString());
        callEnqueue(call, "Пароль изменён");
    }

    private void callEnqueue(Call<Empty> call, String message) {
        call.enqueue(new Callback<Empty>() {
            @Override
            public void onResponse(Call<Empty> call, Response<Empty> response) {
                Log.i(TAG, message);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Empty> call, Throwable t) {
                Log.i(TAG, t.getMessage());
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initUser() {
        userAPI = HelperAPI.getRetrofit().create(UserAPI.class);
        Call<User> call = userAPI.profile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                Log.i(TAG, "Удачное подключение к профилю");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w(TAG, t.getMessage());
                Toast.makeText(getContext(), "Не удалось подключиться к профилю", Toast.LENGTH_SHORT).show();
            }
        });
    }
}