package com.example.our_planner.ui.invitations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Invitation;

import java.util.ArrayList;

public class InvitationsFragment extends Fragment {

    private ArrayList<Invitation> invitations;
    private RecyclerView recyclerViewInvitations;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        InvitationsViewModel invitationsViewModel = new ViewModelProvider(this).get(InvitationsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_invitations, container, false);

        recyclerViewInvitations = view.findViewById(R.id.recyclerViewInvitations);
        recyclerViewInvitations.setLayoutManager(new LinearLayoutManager(getContext()));
        invitations = new ArrayList<>();

        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_launcher_foreground", null, getActivity().getPackageName())));
        invitations.add(new Invitation("Geometry","Marti Lahoz", imageView));
        invitations.add(new Invitation("Calculus","Albert Clop", imageView));
        invitations.add(new Invitation("Pis","Carlos Martin", imageView));

        AdapterInvitations adapterInvitations = new AdapterInvitations(invitations);
        recyclerViewInvitations.setAdapter(adapterInvitations);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}