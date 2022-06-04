package com.example.moneywayapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.OperationAPI;
import com.example.moneywayapp.handler.WalletHandler;
import com.example.moneywayapp.model.context.DateOperationContext;
import com.example.moneywayapp.model.dto.Category;
import com.example.moneywayapp.model.dto.Operation;
import com.example.moneywayapp.model.dto.TypeOperation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomeFragment extends Fragment {

    private static final String TAG = IncomeFragment.class.getSimpleName();

    private EditText nameNewCategoryText;

    private TextView dateText;

    private ListView categoriesListView;

    private CategoryOfUserAPI categoryAPI;

    private OperationAPI operationAPI;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private WalletHandler walletHandler;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public IncomeFragment(WalletHandler walletHandler) {
        super(R.layout.income);
        this.walletHandler = walletHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameNewCategoryText = requireView().findViewById(R.id.editTextAddCategoryIncomeWallet);
        Button addNewCategoryButton = requireView().findViewById(R.id.addCategoryIncomeWalletButton);
        ImageButton pickDateButton = requireView().findViewById(R.id.datePickerIncomeWalletButton);
        dateText = requireView().findViewById(R.id.dateIncomeWalletText);
        categoriesListView = requireView().findViewById(R.id.categoriesWalletListView);

        categoryAPI = HelperAPI.getRetrofitAuth().create(CategoryOfUserAPI.class);
        operationAPI = HelperAPI.getRetrofitAuth().create(OperationAPI.class);

        toDate = LocalDateTime.now();
        fromDate = toDate.minusDays(1);

        addNewCategoryButton.setOnClickListener(this::onClickedAddNewCategoryButton);
        pickDateButton.setOnClickListener(this::onClickedPickerDateButton);

        String fromDateFormatted = fromDate.format(formatter);
        String toDateFormatted = toDate.format(formatter);
        dateText.setText(String.format("%s - %s", fromDateFormatted, toDateFormatted));
        initCategories();
    }

    List<Category> categories = new ArrayList<>();
    List<Double> totals = new ArrayList<>();
    private void initCategories() {
        Runnable task = () -> {
            Call<List<Category>> call = categoryAPI.get();
            Response<List<Category>> response;
            try {
                response = call.execute();
                categories = response.body();
            } catch (IOException e) {
                walletHandler.setTotalMoney(String.format("%s руб", 0));
                Log.w(TAG, e.getMessage());
            }
        };

        Thread thread = new Thread(task);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.w(TAG, e.getMessage());
            return;
        }

        for (Category category : categories) {
            Runnable task2 = () -> {
                DateOperationContext dateOperationContext = new DateOperationContext(category.getId(), fromDate, toDate);
                Call<List<Operation>> operationCall = operationAPI.getByCategoryAndPeriod(dateOperationContext);

                Response<List<Operation>> response;
                try {
                    response = operationCall.execute();
                } catch (IOException e) {
                    Log.w(TAG, e.getMessage());
                    return;
                }

                List<Operation> operations = response.body();
                if (operations == null) {
                    totals.add(0.);
                    return;
                }

                Double total = 0.;
                for (Operation operation : operations) {
                    if (operation.getType().equals(TypeOperation.INCOME)) {
                        total += operation.getValue();
                    }
                }
                totals.add(total);
            };

            Thread thread2 = new Thread(task2);
            thread2.start();
            try {
                thread2.join();
            } catch (InterruptedException e) {
                Log.w(TAG, e.getMessage());
                return;
            }
        }

        double resultTotal = totals.stream().mapToDouble(t -> t).sum();
        walletHandler.setTotalMoney(String.format("%s руб", resultTotal));

        Map<String, Double> categoriesMap = new HashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            categoriesMap.put(categories.get(i).getName(), totals.get(i));
        }

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.category_list_item,
                new String[]{"Name", "Value"},
                new int[]{R.id.categoriesListItemName, R.id.categoriesListItemValue});


        for (Map.Entry<String, Double> stringDoubleEntry : categoriesMap.entrySet()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("Name", stringDoubleEntry.getKey());
            resultsMap.put("Value", stringDoubleEntry.getValue().toString());
            listItems.add(resultsMap);
        }

        categoriesListView.setAdapter(adapter);
    }

    private void onClickedAddNewCategoryButton(View view) {
        Category category = new Category();
        category.setName(nameNewCategoryText.getText().toString());

        Call<Void> call = categoryAPI.add(category);
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
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
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
        initCategories();
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
