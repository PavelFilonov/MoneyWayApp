package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.auth;
import static com.example.moneywayapp.MainActivity.joinThread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.api.GroupAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.api.UserAPI;
import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.model.dto.GroupDTO;
import com.example.moneywayapp.model.dto.UserDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GroupItemUsersFragment extends Fragment {

    private static final String TAG = GroupItemUsersFragment.class.getSimpleName();

    private final GroupDTO group;

    private final UserDTO user;

    private UserDTO owner;

    private GroupAPI groupAPI;

    private final TransitionHandler transitionHandler;

    private List<String> usernames;

    public GroupItemUsersFragment(GroupDTO group, TransitionHandler transitionHandler) {
        super(R.layout.group_item_for_users);

        this.group = group;
        this.user = auth.getUser();
        this.transitionHandler = transitionHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backButton = requireView().findViewById(R.id.backFromGroupButton);
        ImageButton exitButton = requireView().findViewById(R.id.exitGroupButton);
        TextView nameGroupText = requireView().findViewById(R.id.nameGroupItemText);
        ListView usersListView = requireView().findViewById(R.id.usersGroupListView);
        TextView nameOwnerText = requireView().findViewById(R.id.nameOwnerText);

        nameGroupText.setText(group.getName());

        groupAPI = HelperAPI.getRetrofitAuth().create(GroupAPI.class);

        backButton.setOnClickListener(this::onClickedBackButton);
        exitButton.setOnClickListener(this::onClickedExitButton);

        Runnable task = () -> {
            Call<List<String>> callUsers = groupAPI.getUsers(group.getId());
            try {
                Response<List<String>> response = callUsers.execute();
                usernames = response.body();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.group_list_item_for_users,
                new String[]{"Name"},
                new int[]{R.id.usersGroupListItemName});

        initOwner();

        for (String username : usernames) {
            if (username.equals(owner.getLogin())) {
                nameOwnerText.setText(String.format("Основатель: %s", username));
            } else {
                HashMap<String, String> resultsMap = new HashMap<>();
                resultsMap.put("Name", username);
                listItems.add(resultsMap);
            }
        }

        usersListView.setAdapter(adapter);
    }

    private void initOwner() {
        UserAPI userAPI = HelperAPI.getRetrofitAuth().create(UserAPI.class);

        Runnable task = () -> {
            Call<UserDTO> call = userAPI.getLoginById(group.getOwnerId());
            try {
                Response<UserDTO> response = call.execute();
                owner = response.body();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);
    }

    private void onClickedExitButton(View view) {
        Runnable task = () -> {
            Call<Void> exitCall = groupAPI.deleteUser(group.getId(), user.getLogin());
            try {
                Response<Void> response = exitCall.execute();
                if (response.isSuccessful())
                    transitionHandler.moveToGroups();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);
    }

    private void onClickedBackButton(View view) {
        transitionHandler.moveToLastGroupFragment();
    }
}
