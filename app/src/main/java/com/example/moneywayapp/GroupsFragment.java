package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.GroupAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.model.dto.GroupDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    private static final String TAG = GroupsFragment.class.getSimpleName();

    private EditText tokenText, newGroupText;

    private Button searchGroupButton, createGroupButton;

    private ListView groupsListView;

    private GroupAPI groupAPI;

    public GroupsFragment() {
        super(R.layout.groups);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenText = requireView().findViewById(R.id.editTextToken);
        newGroupText = requireView().findViewById(R.id.editTextNewGroup);
        searchGroupButton = requireView().findViewById(R.id.searchGroupButton);
        createGroupButton = requireView().findViewById(R.id.createGroupButton);
        groupsListView = requireView().findViewById(R.id.groupsListView);

        groupAPI = HelperAPI.getRetrofitAuth().create(GroupAPI.class);

        searchGroupButton.setOnClickListener(this::onClickedSearchGroupButton);
        createGroupButton.setOnClickListener(this::onClickedCreateGroupButton);

        Call<List<GroupDTO>> call = groupAPI.getByUser();
        call.enqueue(new Callback<List<GroupDTO>>() {
            @Override
            public void onResponse(Call<List<GroupDTO>> call, Response<List<GroupDTO>> response) {
                List<GroupDTO> groups = response.body();
                List<String> names = new ArrayList<>();
                groups.forEach(group -> names.add(group.getName()));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.group_list_item, names);
                groupsListView.setAdapter(adapter);
                groupsListView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    GroupDTO group = groups.get(i);
                    // TODO: переход к группе
                });
            }

            @Override
            public void onFailure(Call<List<GroupDTO>> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });
    }

    private void onClickedSearchGroupButton(View view) {

    }

    private void onClickedCreateGroupButton(View view) {
        GroupDTO group = new GroupDTO();
        group.setName(newGroupText.getText().toString());
        group.setOwnerId(auth.getUser().getId());

        Call<Void> add = groupAPI.add(group);
        add.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    Log.i(TAG, "Группа добавлена");
                else
                    Log.w(TAG, response.errorBody().toString());

                // TODO: переход в группу
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });
    }
}
