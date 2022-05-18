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
import java.util.Map;

public class AdapterCalendarGroups extends RecyclerView.Adapter<AdapterCalendarGroups.ViewHolderCalendarGroups> {

    private final ArrayList<Group> groups;
    private final Map<String, Boolean> selections;
    private OnGroupListener mOnGroupListener;

    public AdapterCalendarGroups(ArrayList<Group> groups, OnGroupListener onGroupListener, Map<String, Boolean> selections) {
        this.groups = groups;
        this.mOnGroupListener = onGroupListener;
        this.selections = selections;

        for (Group group : groups) {
            if (!selections.containsKey(group.getId()))
                selections.put(group.getId(), true);
        }
        mOnGroupListener.onGroupSelect(selections);

    }

    @NonNull
    @Override
    public ViewHolderCalendarGroups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_calendar_groups,parent,false);
        return new ViewHolderCalendarGroups(view, mOnGroupListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCalendarGroups holder, int position) {
        holder.assignCalendarGroups(groups.get(position));

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public interface OnGroupListener {
        void onGroupSelect(Map<String, Boolean> selections);
    }

    public class ViewHolderCalendarGroups extends RecyclerView.ViewHolder implements View.OnClickListener {
        Group group;
        TextView groupTV;
        CheckBox selected;
        View itemView;
        OnGroupListener onGroupListener;

        public ViewHolderCalendarGroups(@NonNull View itemView, OnGroupListener onGroupListener) {
            super(itemView);
            this.itemView = itemView;
            groupTV = itemView.findViewById(R.id.nameGroup);
            selected = itemView.findViewById(R.id.selectedCalendarGroup);
            this.onGroupListener = onGroupListener;

            selected.setOnClickListener(this);
        }

        public void assignCalendarGroups(Group group) {
            this.group = group;
            this.groupTV.setText(group.getTitle());
            this.selected.setChecked(selections.get(group.getId()));
        }


        @Override
        public void onClick(View view) {
            selections.replace(group.getId(), selected.isChecked());
            onGroupListener.onGroupSelect(selections);
        }
    }
}
