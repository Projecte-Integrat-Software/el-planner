package com.example.our_planner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.model.Invitation;

import java.util.ArrayList;

public class AdapterInvitations extends RecyclerView.Adapter<AdapterInvitations.ViewHolderInvitations> {

    ArrayList<Invitation> invitations;

    public AdapterInvitations(ArrayList<Invitation> invitations) {
        this.invitations = invitations;
    }

    @NonNull
    @Override
    public ViewHolderInvitations onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_invitations,parent,false);
        return new ViewHolderInvitations(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInvitations holder, int position) {
        holder.setData(invitations.get(position));
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public class ViewHolderInvitations extends RecyclerView.ViewHolder {

        ImageView imageInvitation;
        TextView titleInvitation;
        TextView authorInvitation;
        Button acceptInvitationBtn;
        Button denyInvitationBtn;

        public ViewHolderInvitations(@NonNull View itemView) {
            super(itemView);
            imageInvitation = itemView.findViewById(R.id.imageInvitation);
            titleInvitation = itemView.findViewById(R.id.titleInvitation);
            authorInvitation = itemView.findViewById(R.id.authorInvitation);
            acceptInvitationBtn = itemView.findViewById(R.id.acceptInvitationBtn);
            denyInvitationBtn = itemView.findViewById(R.id.denyInvitationBtn);
        }

        public void setData(Invitation invitation) {
            imageInvitation = invitation.getImage();
            titleInvitation.setText(invitation.getTitle());
            authorInvitation.setText(invitation.getAuthor());
        }
    }
}
