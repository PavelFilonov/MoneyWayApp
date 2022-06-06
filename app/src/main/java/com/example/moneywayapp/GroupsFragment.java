package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.auth;
import static com.example.moneywayapp.MainActivity.joinThread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.GroupAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.model.dto.CategoryDTO;
import com.example.moneywayapp.model.dto.GroupDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    private static final String TAG = GroupsFragment.class.getSimpleName();

    private EditText tokenText, newGroupText;

    private Button searchGroupButton, createGroupButton;

    private ListView groupsListView;

    private GroupAPI groupAPI;

    private final TransitionHandler transitionHandler;

    public GroupsFragment(TransitionHandler transitionHandler) {
        super(R.layout.groups);
        this.transitionHandler = transitionHandler;
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

        initGroups();
    }

    List<GroupDTO> groups;

    private void initGroups() {
        groups = new ArrayList<>();

        Runnable task = () -> {
            Call<List<GroupDTO>> call = groupAPI.getByUser();
            Response<List<GroupDTO>> response;
            try {
                response = call.execute();
                groups = response.body();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };

        joinThread(task);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.group_list_item,
                new String[]{"Name"},
                new int[]{R.id.groupsListItemName});


        Map<Integer, Long> positionIdMap = new HashMap<>();

        int position = 0;
        for (int i = groups.size() - 1; i >= 0; i--) {
            HashMap<String, String> resultsMap = new HashMap<>();
            String name = groups.get(i).getName();
            resultsMap.put("Name", name);
            positionIdMap.put(position, groups.get(i).getId());
            position++;
            listItems.add(resultsMap);
        }

        groupsListView.setAdapter(adapter);

        groupsListView.setOnItemClickListener((adapterView, view, i, l) -> {
            transitionHandler.moveToGroup(findGroupById(positionIdMap.get(i)));
        });
    }

    private GroupDTO findGroupById(Long id) {
        for (GroupDTO group: groups) {
            if (group.getId().equals(id))
                return group;
        }
        return null;
    }

    private void onClickedSearchGroupButton(View view) {
    }

    private void onClickedCreateGroupButton(View view) {
        GroupDTO group = new GroupDTO();
        group.setName(newGroupText.getText().toString());
        group.setOwnerId(auth.getUser().getId());

        Runnable task = () -> {
            Call<GroupDTO> add = groupAPI.add(group);
            Response<GroupDTO> response;
            try {
                response = add.execute();
                GroupDTO addedGroup = response.body();
                transitionHandler.moveToGroup(addedGroup);
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
        }
    }
}
