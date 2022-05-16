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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdapterCalendarGroups extends RecyclerView.Adapter<AdapterCalendarGroups.ViewHolderCalendarGroups> {

    private final ArrayList<Group> groups;
    private final Map<Group, Boolean> selections;

    public AdapterCalendarGroups(ArrayList<Group> groups) {
        this.groups = groups;
        selections = new HashMap<>();
        Iterator<Group> it = groups.iterator();
        while (it.hasNext()) {
            selections.put((Group) it.next(), true);
        }

    }

    @NonNull
    @Override
    public ViewHolderCalendarGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_calendar_groups,parent,false);
        return new ViewHolderCalendarGroups(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCalendarGroups holder, int position) {
        holder.assignCalendarGroups(groups.get(position));

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolderCalendarGroups extends RecyclerView.ViewHolder {
        Group group;
        TextView groupTV;
        CheckBox selected;
        View itemView;

        public ViewHolderCalendarGroups(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            groupTV = itemView.findViewById(R.id.nameGroup);
            selected = itemView.findViewById(R.id.selectedCalendarGroup);
        }

        public void assignCalendarGroups(Group group) {
            this.group = group;
            this.groupTV.setText(group.getTitle());
            this.selected.setChecked(selections.get(group));
        }


    }
}
