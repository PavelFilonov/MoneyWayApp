package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.user;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.model.dto.Category;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseFragment extends Fragment {

    private static final String TAG = ExpenseFragment.class.getSimpleName();

    private EditText nameNewCategoryText;

    private TextView dateText;

    private CategoryOfUserAPI categoryAPI;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ExpenseFragment() {
        super(R.layout.expense);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameNewCategoryText = requireView().findViewById(R.id.editTextAddCategoryExpenseWallet);
        Button addNewCategoryButton = requireView().findViewById(R.id.addCategoryExpenseWalletButton);
        ImageButton pickDateButton = requireView().findViewById(R.id.datePickerExpenseWalletButton);
        dateText = requireView().findViewById(R.id.dateExpenseWalletText);

        categoryAPI = HelperAPI.getRetrofit().create(CategoryOfUserAPI.class);

        toDate = LocalDateTime.now();
        fromDate = toDate.minusDays(1);

        addNewCategoryButton.setOnClickListener(this::onClickedAddNewCategoryButton);
        pickDateButton.setOnClickListener(this::onClickedPickerDateButton);

        String fromDateFormatted = fromDate.format(formatter);
        String toDateFormatted = toDate.format(formatter);
        dateText.setText(String.format("%s - %s", fromDateFormatted, toDateFormatted));
    }

    private void onClickedAddNewCategoryButton(View view) {
        Category category = new Category();
        category.setName(nameNewCategoryText.getText().toString());

        Call<Void> call = categoryAPI.add(new UserCategoryContext(user, category));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String msg = "Категория " + category.getName() + " добавлена";

                switch (response.code()) {
                    case 422:
                        Log.i(TAG, response.message());
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        break;
                    case 200:
                        Log.i(TAG, msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });
    }


    private void onClickedPickerDateButton(View view) {
        pickDateAndTime(toDate);
        pickDateAndTime(fromDate);

        String fromDateFormatted = fromDate.format(formatter);
        String toDateFormatted = toDate.format(formatter);
        dateText.setText(String.format("%s - %s", fromDateFormatted, toDateFormatted));
    }

    private void pickDateAndTime(LocalDateTime dateTime) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i3, i11) -> {
            setPickedTime(i3, i11, dateTime);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
        timePickerDialog.show();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
            setPickedDate(i, i1, i2, dateTime);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setPickedDate(int i, int i1, int i2, LocalDateTime dateTime) {
        dateTime.withYear(i);
        dateTime.withMonth(i1 + 1);
        dateTime.withDayOfMonth(i2);
    }

    private void setPickedTime(int i, int i1, LocalDateTime dateTime) {
        dateTime.withHour(i);
        dateTime.withMinute(i1);
    }
}
