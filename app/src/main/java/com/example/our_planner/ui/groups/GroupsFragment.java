package com.example.our_planner.ui.groups;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupsFragment extends Fragment {

    private RecyclerView recyclerViewGroups;
    private GroupsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        viewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        recyclerViewGroups = view.findViewById(R.id.recyclerViewGroups);
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton addGroupBtn = view.findViewById(R.id.addGroupBtn);
        addGroupBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateGroupActivity.class)));

        //We add the group if added or edit it if edited
        Intent i = getActivity().getIntent();
        Group g = (Group) i.getSerializableExtra("group");
        if (g != null) {
            int pos = i.getIntExtra("groupPosition", -1);
            if (pos != -1) {
                viewModel.editGroup(g, pos);
            } else {
                viewModel.addGroup(g);
            }
        }

        recyclerViewGroups.setAdapter(new AdapterGroups(viewModel.getGroups()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}