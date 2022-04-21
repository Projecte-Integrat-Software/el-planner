package com.example.our_planner.ui.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.User;

import java.util.ArrayList;

public class AdapterParticipants extends RecyclerView.Adapter<AdapterParticipants.ViewHolderParticipants>{

    private final ArrayList<User> participants;

    public AdapterParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    @NonNull
    @Override
    public ViewHolderParticipants onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_participants,parent,false);
        return new ViewHolderParticipants(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderParticipants holder, int position) {
        holder.setData(participants.get(position));
        holder.adminCheckBox.setOnClickListener(view -> {
            //TODO: Change the permission of the user when group created
            Toast.makeText(view.getContext(), "Changing admin permission", Toast.LENGTH_LONG).show();
        });
        holder.expelParticipantBtn.setOnClickListener(view -> {
            //TODO: Open confirmation to expel participant from group
            Toast.makeText(view.getContext(), "Expelling from group", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public static class ViewHolderParticipants extends RecyclerView.ViewHolder {

        private final TextView participantTxt;
        private final CheckBox adminCheckBox;
        private final ImageButton expelParticipantBtn;

        public ViewHolderParticipants(@NonNull View itemView) {
            super(itemView);
            participantTxt = itemView.findViewById(R.id.txtParticipant);
            adminCheckBox = itemView.findViewById(R.id.cbAdmin);
            expelParticipantBtn = itemView.findViewById(R.id.expelParticipantBtn);
        }

        public void setData(User participant) {
            participantTxt.setText(participant.getUsername());
        }
    }
}
