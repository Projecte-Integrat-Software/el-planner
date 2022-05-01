package com.example.our_planner.ui.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;

import java.util.ArrayList;

public class AdapterParticipantsInvite extends RecyclerView.Adapter<AdapterParticipantsInvite.ViewHolderParticipantsInvite> {

    private final ArrayList<String> participantEmails;

    public AdapterParticipantsInvite(ArrayList<String> participantEmails) {
        this.participantEmails = new ArrayList<>(participantEmails);
    }

    @NonNull
    @Override
    public ViewHolderParticipantsInvite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_participants_invite, parent, false);
        return new ViewHolderParticipantsInvite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderParticipantsInvite holder, int position) {
        holder.setData(participantEmails.get(position));
        holder.removeInvitationBtn.setOnClickListener(view -> {
            participantEmails.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        });
    }

    public boolean addElement(String participant) {
        boolean b = !participantEmails.contains(participant);
        if (b) {
            participantEmails.add(participant);
            int n = getItemCount();
            notifyItemInserted(n);
            notifyItemRangeChanged(n, n);
        }
        return b;
    }

    public ArrayList<String> getParticipantEmails() {
        return participantEmails;
    }

    @Override
    public int getItemCount() {
        return participantEmails.size();
    }

    public static class ViewHolderParticipantsInvite extends RecyclerView.ViewHolder {

        private final TextView emailTxt;
        private final ImageButton removeInvitationBtn;

        public ViewHolderParticipantsInvite(@NonNull View itemView) {
            super(itemView);
            emailTxt = itemView.findViewById(R.id.txtInvitationEmail);
            removeInvitationBtn = itemView.findViewById(R.id.removeInvitationBtn);
        }

        public void setData(String email) {
            emailTxt.setText(email);
        }
    }
}
