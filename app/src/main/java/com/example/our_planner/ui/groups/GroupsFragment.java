package com.example.our_planner.ui.groups;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

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

        Intent i = getActivity().getIntent();
        if (i != null) {
            Group g = (Group) i.getSerializableExtra("group");
            if (g != null) {
                viewModel.addGroup(g);
            }
        }

        MutableLiveData<ArrayList<Group>> groups = viewModel.getGroups();
        recyclerViewGroups.setAdapter(new AdapterGroups(groups.getValue()));
        final Observer<ArrayList<Group>> observer = g -> {
            AdapterGroups newAdapter = new AdapterGroups(g);
            recyclerViewGroups.swapAdapter(newAdapter, false);
            newAdapter.notifyDataSetChanged();
        };

        groups.observe(getViewLifecycleOwner(), observer);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}