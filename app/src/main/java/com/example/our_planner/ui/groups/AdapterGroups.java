package com.example.our_planner.ui.groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;
import com.example.our_planner.model.Invitation;

import java.util.ArrayList;

public class AdapterGroups extends RecyclerView.Adapter<ViewHolderGroups> {

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
        holder.getEditGroupBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Open edit group activity (only if admin!)
                Toast.makeText(holder.itemView.getContext(), "Editing group", Toast.LENGTH_LONG).show();
            }
        });
        holder.getLeaveGroupBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Open confirmation to leave group
                Toast.makeText(holder.itemView.getContext(), "Leaving group", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
