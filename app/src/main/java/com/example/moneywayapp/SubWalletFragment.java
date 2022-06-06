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
import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.handler.WalletHandler;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.OperationDTO;
import com.example.moneywayapp.model.dto.TypeOperation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubWalletFragment extends Fragment {

    private static final String TAG = SubWalletFragment.class.getSimpleName();

    private EditText nameNewCategoryText;

    private TextView dateText;

    private ListView categoriesListView;

    private CategoryOfUserAPI categoryAPI;

    private OperationAPI operationAPI;

    public static LocalDateTime fromDate;

    public static LocalDateTime toDate;

    private WalletHandler walletHandler;

    private TransitionHandler transitionHandler;

    private TypeOperation type;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");

    public SubWalletFragment(TypeOperation type, WalletHandler walletHandler, TransitionHandler transitionHandler) {
        super(R.layout.sub_wallet);
        this.type = type;
        this.walletHandler = walletHandler;
        this.transitionHandler = transitionHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameNewCategoryText = requireView().findViewById(R.id.editTextAddCategoryWallet);
        Button addNewCategoryButton = requireView().findViewById(R.id.addCategoryWalletButton);
        ImageButton pickDateButton = requireView().findViewById(R.id.datePickerWalletButton);
        dateText = requireView().findViewById(R.id.dateWalletText);
        categoriesListView = requireView().findViewById(R.id.categoriesWalletListView);

        categoryAPI = HelperAPI.getRetrofitAuth().create(CategoryOfUserAPI.class);
        operationAPI = HelperAPI.getRetrofitAuth().create(OperationAPI.class);

        if (toDate == null)
            toDate = LocalDateTime.now();

        if (fromDate == null)
            fromDate = toDate.minusDays(1);

        addNewCategoryButton.setOnClickListener(this::onClickedAddNewCategoryButton);
        pickDateButton.setOnClickListener(this::onClickedPickerDateButton);

        String fromDateFormatted = fromDate.format(formatter);
        String toDateFormatted = toDate.format(formatter);
        dateText.setText(String.format("%s - %s", fromDateFormatted, toDateFormatted));
        initCategories();
    }

    List<CategoryDTO> categories = new ArrayList<>();
    List<Double> totals = new ArrayList<>();

    private void initCategories() {
        categories = new ArrayList<>();
        totals = new ArrayList<>();

        Runnable task = () -> {
            Call<List<CategoryDTO>> call = categoryAPI.get();
            Response<List<CategoryDTO>> response;
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
                if (operations == null) {
                    totals.add(0.);
                    return;
                }

                Double total = 0.;
                for (OperationDTO operation : operations) {
                    if (operation.getType().equals(type)) {
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
        Map<Long, Long> positionIdMap = new HashMap<>();
        int size = 0;
        for (int i = 0; i < categories.size(); i++) {
            if (!categoriesMap.containsKey(categories.get(i).getName())) {
                categoriesMap.put(categories.get(i).getName(), totals.get(i));
                positionIdMap.put((long) size, categories.get(i).getId());
                size++;
            }
        }

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.category_list_item,
                new String[]{"Name", "Value"},
                new int[]{R.id.categoriesListItemName, R.id.categoriesListItemValue});


        for (int i = 0; i < size; i++) {
            HashMap<String, String> resultsMap = new HashMap<>();
            String name = findNameById(positionIdMap.get((long) i));
            String value = categoriesMap.get(name).toString();
            resultsMap.put("Name", name);
            resultsMap.put("Value", value);
            listItems.add(resultsMap);
        }

        categoriesListView.setAdapter(adapter);

        categoriesListView.setOnItemClickListener((adapterView, view, i, l) -> {
            transitionHandler.moveToCategory(findCategoryById(positionIdMap.get(l)), type);
        });
    }

    private String findNameById(Long id) {
        for (CategoryDTO category : categories) {
            if (category.getId().equals(id))
                return category.getName();
        }
        return null;
    }

    private CategoryDTO findCategoryById(Long id) {
        for (CategoryDTO category : categories) {
            if (category.getId().equals(id))
                return category;
        }
        return null;
    }

    private void onClickedAddNewCategoryButton(View view) {
        CategoryDTO category = new CategoryDTO();
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
                    case 201:
                        Log.i(TAG, msg);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        initCategories();
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
                        initCategories();

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
