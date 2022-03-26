package com.example.our_planner.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {

    private RecyclerView recyclerViewGroups;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //GroupsViewModel groupsViewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        recyclerViewGroups = view.findViewById(R.id.recyclerViewGroups);
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        //Dummy list to run the test of the recycler view without the database
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group("Geometry"));
        groups.add(new Group("Calculus"));
        groups.add(new Group("Pis"));
        AdapterGroups adapterGroups = new AdapterGroups(groups);
        recyclerViewGroups.setAdapter(adapterGroups);

        FloatingActionButton addGroupBtn = view.findViewById(R.id.addGroupBtn);
        addGroupBtn.setOnClickListener(view1 -> {
            //TODO: Add new group activity
            Toast.makeText(view1.getContext(), "Adding group", Toast.LENGTH_LONG).show();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}