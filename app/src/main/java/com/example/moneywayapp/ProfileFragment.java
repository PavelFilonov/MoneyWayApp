package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.auth;
import static com.example.moneywayapp.MainActivity.userAPI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.handler.TransitionHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private EditText emailText, loginText, passwordText;

    private final TransitionHandler transitionHandler;

    public ProfileFragment(TransitionHandler transitionHandler) {
        super(R.layout.profile);
        this.transitionHandler = transitionHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView loginTextView = requireView().findViewById(R.id.usernameProfileTextView);
        emailText = requireView().findViewById(R.id.editTextProfileEmail);
        loginText = requireView().findViewById(R.id.editTextProfileUsername);
        passwordText = requireView().findViewById(R.id.editTextProfilePassword);
        ImageButton updateEmailButton = requireView().findViewById(R.id.updateEmailButton);
        ImageButton updateLoginButton = requireView().findViewById(R.id.updateLoginButton);
        ImageButton updatePasswordButton = requireView().findViewById(R.id.updatePasswordButton);

        loginTextView.setText(auth.getUser().getLogin());

        updateEmailButton.setOnClickListener(this::onClickedUpdateEmailButton);
        updateLoginButton.setOnClickListener(this::onClickedUpdateLoginButton);
        updatePasswordButton.setOnClickListener(this::onClickedUpdatePasswordButton);
    }

    private void onClickedUpdateEmailButton(View view) {
        Call<Void> call = userAPI.updateEmail(emailText.getText().toString());
        callEnqueue(call, "Email изменён", "Email уже используется");
    }

    private void onClickedUpdateLoginButton(View view) {
        Call<Void> call = userAPI.updateLogin(loginText.getText().toString());
        callEnqueue(call, "Логин изменён", "Логин уже используется");
    }

    private void onClickedUpdatePasswordButton(View view) {
        Call<Void> call = userAPI.updatePassword(passwordText.getText().toString());
        callEnqueue(call, "Пароль изменён", null);
    }

    private void callEnqueue(Call<Void> call, String successMessage, String alreadyExistsMessage) {
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 422:
                        Log.i(TAG, response.message());
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                        break;
                    case 409:
                        Log.i(TAG, alreadyExistsMessage);
                        Toast.makeText(getContext(), alreadyExistsMessage, Toast.LENGTH_SHORT).show();
                        break;
                    case 200:
                        if (successMessage.contains("Email"))
                            auth.getUser().setEmail(emailText.getText().toString());

                        if (successMessage.contains("Логин"))
                            auth.getUser().setEmail(loginText.getText().toString());

                        if (successMessage.contains("Пароль"))
                            auth.getUser().setEmail(passwordText.getText().toString());

                        Log.i(TAG, successMessage);
                        Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show();

                        transitionHandler.moveToProfile();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }
}