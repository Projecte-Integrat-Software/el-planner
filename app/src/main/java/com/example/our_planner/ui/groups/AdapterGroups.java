package com.example.our_planner.ui.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class AdapterGroups extends RecyclerView.Adapter<AdapterGroups.ViewHolderGroups> {

    private final ArrayList<Group> groups;

    public AdapterGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolderGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_groups,parent,false);
        return new ViewHolderGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderGroups holder, int position) {
        holder.setData(groups.get(position));
        holder.editGroupBtn.setOnClickListener(view -> {
            //TODO: Open edit group activity (only if admin!)
            Toast.makeText(view.getContext(), "Editing group", Toast.LENGTH_LONG).show();
        });
        holder.leaveGroupBtn.setOnClickListener(view -> {
            //TODO: Open confirmation to leave group
            Toast.makeText(view.getContext(), "Leaving group", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class ViewHolderGroups extends RecyclerView.ViewHolder {

        private final TextView titleGroupTxt;
        private final Button editGroupBtn;
        private final ImageButton leaveGroupBtn;

        public ViewHolderGroups(@NonNull View itemView) {
            super(itemView);
            titleGroupTxt = itemView.findViewById(R.id.titleGroup);
            editGroupBtn = itemView.findViewById(R.id.editGroupBtn);
            leaveGroupBtn = itemView.findViewById(R.id.leaveGroupBtn);
        }

        public void setData(Group group) {
            titleGroupTxt.setText(group.getTitle());
        }
    }
}
