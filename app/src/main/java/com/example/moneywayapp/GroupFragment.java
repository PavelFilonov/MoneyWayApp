package com.example.moneywayapp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneywayapp.model.dto.GroupDTO;

public class GroupFragment extends Fragment {

    private GroupDTO group;

    public GroupFragment(GroupDTO group) {
        super(R.layout.group);
        this.group = group;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}