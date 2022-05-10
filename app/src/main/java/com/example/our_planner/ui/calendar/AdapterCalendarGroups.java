package com.example.our_planner.ui.calendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class AdapterCalendarGroups extends RecyclerView.Adapter<AdapterCalendarGroups.ViewHolderCalendarGroups> {

    private final ArrayList<Group> groups;

    public AdapterCalendarGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolderCalendarGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_calendar_groups,parent,false);
        return new ViewHolderCalendarGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCalendarGroups holder, int position) {
        holder.asignarCalendarGroups(groups.get(position));

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolderCalendarGroups extends RecyclerView.ViewHolder {
        TextView group;
        CheckBox selected;

        public ViewHolderCalendarGroups(@NonNull View itemView) {
            super(itemView);
            group = itemView.findViewById(R.id.nameGroup);
            selected = itemView.findViewById(R.id.selectedCalendarGroup);
        }

        public void asignarCalendarGroups(Group group) {
            this.group.setText(group.getTitle());
        }
    }
}
