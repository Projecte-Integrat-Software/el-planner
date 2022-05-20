package com.example.our_planner.ui.groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    private Context parentContext;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        viewModel = new ViewModelProvider(this).get(GroupsViewModel.class);
        parentContext = getContext();
        recyclerViewGroups = view.findViewById(R.id.recyclerViewGroups);
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(getContext()));

        final Observer<String> observerToast = t -> Toast.makeText(getContext(), t, Toast.LENGTH_SHORT).show();
        viewModel.getToast().observe(getActivity(), observerToast);

        FloatingActionButton addGroupBtn = view.findViewById(R.id.addGroupBtn);
        addGroupBtn.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), CreateGroupActivity.class)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Observer<ArrayList<Group>> observerGroups = g -> changeAdapter(g);
        changeAdapter(viewModel.getGroups().getValue());
        viewModel.getGroups().removeObservers(getActivity());
        viewModel.getGroups().observe(getActivity(), observerGroups);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void changeAdapter(ArrayList<Group> g) {
        AdapterGroups newAdapter = new AdapterGroups(parentContext, g);
        recyclerViewGroups.swapAdapter(newAdapter, false);
        newAdapter.notifyDataSetChanged();
    }
}