package com.example.our_planner.ui.invitations;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.R;
import com.example.our_planner.model.Invitation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
        Invitation i = invitations.get(position);
        holder.setData(i);
        holder.acceptInvitationBtn.setOnClickListener(v -> {
            DataBaseAdapter.acceptInvitation(i);
            invitations.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        });
        holder.declineInvitationBtn.setOnClickListener(v -> {
            DataBaseAdapter.deleteInvitation(i);
            invitations.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        });
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public static class ViewHolderInvitations extends RecyclerView.ViewHolder {

        ImageView imageInvitation;
        TextView titleInvitation;
        TextView authorInvitation;
        ImageButton acceptInvitationBtn;
        ImageButton declineInvitationBtn;

        public ViewHolderInvitations(@NonNull View itemView) {
            super(itemView);
            imageInvitation = itemView.findViewById(R.id.imageInvitation);
            titleInvitation = itemView.findViewById(R.id.titleInvitation);
            authorInvitation = itemView.findViewById(R.id.authorInvitation);
            acceptInvitationBtn = itemView.findViewById(R.id.acceptInvitationBtn);
            declineInvitationBtn = itemView.findViewById(R.id.declineInvitationBtn);
        }

        public void setData(Invitation invitation) {
            titleInvitation.setText(invitation.getTitle());
            authorInvitation.setText(invitation.getAuthor());
            Task<byte[]> task = DataBaseAdapter.getImage(invitation.getAuthor());
            task.addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    imageInvitation.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            });
        }
    }
}
