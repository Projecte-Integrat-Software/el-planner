package com.example.our_planner.ui.invitations;

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
import com.example.our_planner.model.Invitation;

import java.util.ArrayList;

public class InvitationsFragment extends Fragment {

    private RecyclerView recyclerViewInvitations;
    private InvitationsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(InvitationsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_invitations, container, false);

        recyclerViewInvitations = view.findViewById(R.id.recyclerViewInvitations);
        recyclerViewInvitations.setLayoutManager(new LinearLayoutManager(getContext()));

        final Observer<ArrayList<Invitation>> observerInvitations = i -> {
            AdapterInvitations newAdapter = new AdapterInvitations(i);
            recyclerViewInvitations.swapAdapter(newAdapter, false);
            newAdapter.notifyDataSetChanged();
        };
        final Observer<String> observerToast = t -> Toast.makeText(getContext(), t, Toast.LENGTH_SHORT).show();
        viewModel.getInvitations().observe(getActivity(), observerInvitations);
        viewModel.getToast().observe(getActivity(), observerToast);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}