package com.example.moneywayapp;

import static com.example.moneywayapp.MainActivity.auth;
import static com.example.moneywayapp.MainActivity.joinThread;

import android.app.AlertDialog;
import android.os.Bundle;
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

import com.example.moneywayapp.api.GroupAPI;
import com.example.moneywayapp.api.HelperAPI;
import com.example.moneywayapp.handler.TransitionHandler;
import com.example.moneywayapp.model.dto.GroupDTO;
import com.example.moneywayapp.model.dto.UserDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class GroupItemOwnerFragment extends Fragment {

    private static final String TAG = GroupItemOwnerFragment.class.getSimpleName();

    private final GroupDTO group;

    private final UserDTO user;

    private TextView nameGroupText;

    private EditText newNameGroupText;

    private final TransitionHandler transitionHandler;

    private GroupAPI groupAPI;

    private List<String> usernames;

    public GroupItemOwnerFragment(GroupDTO group, TransitionHandler transitionHandler) {
        super(R.layout.group_item_for_owner);

        this.group = group;
        this.user = auth.getUser();
        this.transitionHandler = transitionHandler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backButton = requireView().findViewById(R.id.backFromOwnerGroupButton);
        ImageButton deleteGroupButton = requireView().findViewById(R.id.deleteGroupButton);
        ImageButton copyTokenButton = requireView().findViewById(R.id.copyTokenGroupButton);
        Button renameGroupButton = requireView().findViewById(R.id.renameGroupButton);
        nameGroupText = requireView().findViewById(R.id.nameOwnerGroupText);
        TextView tokenText = requireView().findViewById(R.id.tokenText);
        ListView ownerGroupListView = requireView().findViewById(R.id.ownerGroupListView);
        newNameGroupText = requireView().findViewById(R.id.editTextNewNameGroup);
        TextView nameOwnerText = requireView().findViewById(R.id.nameOwnerText2);

        nameGroupText.setText(group.getName());
        tokenText.setText(group.getToken());
        nameOwnerText.setText(String.format("Основатель: %s", user.getLogin()));

        groupAPI = HelperAPI.getRetrofitAuth().create(GroupAPI.class);

        backButton.setOnClickListener(this::onClickedBackButton);
        deleteGroupButton.setOnClickListener(this::onClickedDeleteGroupButton);
        copyTokenButton.setOnClickListener(this::onClickedCopyTokenButton);
        renameGroupButton.setOnClickListener(this::onClickedRenameGroupButton);

        Runnable task = () -> {
            Call<List<String>> usersCall = groupAPI.getUsers(group.getId());
            try {
                Response<List<String>> execute = usersCall.execute();
                usernames = execute.body();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);

        if (usernames == null)
            usernames = new ArrayList<>();

        List<HashMap<String, String>> listItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), listItems, R.layout.group_list_item_for_owner,
                new String[]{"Name"},
                new int[]{R.id.usersGroupOwnerListItemName});

        Map<Integer, String> positionIdMap = new HashMap<>();

        int position = 0;
        for (String username : usernames) {
            if (username.equals(auth.getUser().getLogin()))
                continue;

            HashMap<String, String> resultsMap = new HashMap<>();
            resultsMap.put("Name", username);
            positionIdMap.put(position, username);
            position++;
            listItems.add(resultsMap);
        }

        ownerGroupListView.setAdapter(adapter);

        ownerGroupListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            deleteUser(positionIdMap.get(i));
        });
    }

    private void deleteUser(String username) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle(String.format("Удалить пользователя %s?", username));

        ad.setPositiveButton("Да", (dialog, arg1) -> {
            Runnable task = () -> {
                Call<Void> deleteUser = groupAPI.deleteUser(group.getId(), username);
                try {
                    deleteUser.execute();
                    transitionHandler.moveToGroupItem(group);
                } catch (IOException e) {
                    Log.w(TAG, e.getMessage());
                }
            };
            joinThread(task);

            Log.i(TAG, String.format("Пользователь %s удалён", username));
        });

        ad.setNegativeButton("Нет", (dialog, arg1) -> {
            Log.i(TAG, String.format("Пользователь %s не удалён", username));
        });

        ad.show();
    }

    private void onClickedRenameGroupButton(View view) {
        String name = newNameGroupText.getText().toString();

        Runnable task = () -> {
            Call<Void> rename = groupAPI.rename(group.getId(), name);
            try {
                rename.execute();
            } catch (IOException e) {
                Log.w(TAG, e.getMessage());
            }
        };
        joinThread(task);

        nameGroupText.setText(name);
    }

    private void onClickedCopyTokenButton(View view) {
        transitionHandler.copyText(group.getToken());
        Toast.makeText(getContext(), "Токен скопирован", Toast.LENGTH_LONG).show();
    }

    private void onClickedDeleteGroupButton(View view) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setTitle(String.format("Удалить группу %s?", group.getName()));

        ad.setPositiveButton("Да", (dialog, arg1) -> {
            Runnable task = () -> {
                Call<Void> deleteCall = groupAPI.deleteById(group.getId());
                try {
                    deleteCall.execute();
                } catch (IOException e) {
                    Log.w(TAG, e.getMessage());
                }
            };
            joinThread(task);

            transitionHandler.moveToGroups();
        });

        ad.setNegativeButton("Нет", (dialog, arg1) -> {
            Log.i(TAG, String.format("Группа %s не удалена", group.getName()));
        });

        ad.show();
    }

    private void onClickedBackButton(View view) {
        transitionHandler.moveToLastGroupFragment();
    }
}
