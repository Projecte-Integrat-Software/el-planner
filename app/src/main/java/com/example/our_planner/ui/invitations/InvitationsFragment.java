package com.example.our_planner.ui.invitations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.our_planner.databinding.FragmentInvitationsBinding;

public class InvitationsFragment extends Fragment {

    private FragmentInvitationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InvitationsViewModel invitationsViewModel =
                new ViewModelProvider(this).get(InvitationsViewModel.class);

        binding = FragmentInvitationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textInvitations;
        invitationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}