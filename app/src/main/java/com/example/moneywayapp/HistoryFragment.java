package com.example.moneywayapp;

import static com.example.moneywayapp.SubWalletFragment.formatter;
import static com.example.moneywayapp.SubWalletFragment.fromDate;
import static com.example.moneywayapp.SubWalletFragment.toDate;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.CategoryOfUserAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.OperationAPI;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.OperationDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private static final String TAG = HistoryFragment.class.getSimpleName();

    private TextView dateText;

    private ListView historyListView;

    private CategoryOfUserAPI categoryOfUserAPI;

    private OperationAPI operationAPI;

    public HistoryFragment() {
        super(R.layout.history);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyListView = requireView().findViewById(R.id.historyWalletListView);
        ImageButton pickDateButton = requireView().findViewById(R.id.datePickerWalletHistoryButton);
        dateText = requireView().findViewById(R.id.dateWalletHistoryText);

        categoryOfUserAPI = HelperAPI.getRetrofitAuth().create(CategoryOfUserAPI.class);
        operationAPI = HelperAPI.getRetrofitAuth().create(OperationAPI.class);

        pickDateButton.setOnClickListener(this::onClickedPickerDateButton);

        String fromDateFormatted = fromDate.format(formatter);
        String toDateFormatted = toDate.format(formatter);
        dateText.setText(String.format("%s - %s", fromDateFormatted, toDateFormatted));
        initHistory();
    }

    List<CategoryDTO> categories = new ArrayList<>();
    List<OperationDTO> operations = new ArrayList<>();

    private void initHistory() {
        categories = new ArrayList<>();
        operations = new ArrayList<>();

        Runnable task = () -> {
            Call<List<CategoryDTO>> call = categoryOfUserAPI.get();
            Response<List<CategoryDTO>> response;
            try {
                response = call.execute();
                categories = response.body();
            } catch (IOException e) {
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

        for (CategoryDTO category : categories) {
            Runnable task2 = () -> {
                Call<List<OperationDTO>> operationCall = operationAPI.getByCategoryAndPeriod(
                        category.getId(), fromDate.toString(), toDate.toString());

                Response<List<OperationDTO>> response;
                try {
                    response = operationCall.execute();
                } catch (IOException e) {
                    Log.w(TAG, e.getMessage());
                    return;
                }

                List<OperationDTO> operations = response.body();
                if (operations != null)
                    this.operations.addAll(operations);
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

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.history_list_item,
                new String[]{"Name", "Value", "Date"},
                new int[]{R.id.historyListItemName, R.id.historyListItemValue, R.id.historyListItemDate});

        for (int i = operations.size() - 1; i >= 0; i--) {
            HashMap<String, String> resultsMap = new HashMap<>();

            String name = "";
            name += operations.get(i).getCategory().getName();
            name += " (";
            name += operations.get(i).getUser().getLogin();
            name += ")";

            Double value = operations.get(i).getValue();
            if (operations.get(i).getType().equals(TypeOperation.EXPENSE))
                value *= -1;

            LocalDateTime dateTime = LocalDateTime.parse(operations.get(i).getCreatedAt());

            resultsMap.put("Name", name);
            resultsMap.put("Value", value.toString());
            resultsMap.put("Date", dateTime.format(formatter));
            listItems.add(resultsMap);
        }

        historyListView.setAdapter(adapter);
    }

    private void onClickedPickerDateButton(View view) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i3, i11) -> {
                fromDate = LocalDateTime.of(i, i1 + 1, i2, i3, i11);

                DatePickerDialog datePickerDialog_ = new DatePickerDialog(getContext(), (datePicker_, i_, i1_, i2_) -> {

                    TimePickerDialog timePickerDialog_ = new TimePickerDialog(getContext(), (timePicker_, i3_, i11_) -> {
                        toDate = LocalDateTime.of(i_, i1_ + 1, i2_, i3_, i11_);

                        String fromDateFormatted = fromDate.format(formatter);
                        String toDateFormatted = toDate.format(formatter);
                        dateText.setText(String.format("%s - %s", fromDateFormatted, toDateFormatted));
                        initHistory();

                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
                    timePickerDialog_.show();

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog_.show();

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}