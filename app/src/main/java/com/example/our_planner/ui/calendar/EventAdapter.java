package com.example.our_planner.ui.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.R;
import com.example.our_planner.model.Event;
import com.example.our_planner.model.Group;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvents> {

    private final ArrayList<Event> events;
    private final Context context;
    private final OnNoteListener mOnNoteListener;
    private final MutableLiveData<ArrayList<Group>> groups;

    public EventAdapter(Context context, ArrayList<Event> events, OnNoteListener onNoteListener, MutableLiveData<ArrayList<Group>> groups) {
        this.context = context;
        this.events = events;
        this.mOnNoteListener = onNoteListener;
        this.groups = groups;
    }

    @NonNull
    @Override
    public ViewHolderEvents onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false);
        return new ViewHolderEvents(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvents holder, int position) {
        Event e = events.get(position);
        holder.setEvent(e);
        holder.setData(e, groups);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface OnNoteListener {
        void onNoteClick(int position, Event event);
    }

    public static class ViewHolderEvents extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView eventNameTV;
        private final TextView startTimeTV;
        private final View groupColourView;
        private final OnNoteListener onNoteListener;
        private Event event;

        public ViewHolderEvents(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            eventNameTV = itemView.findViewById(R.id.eventNameTV);
            startTimeTV = itemView.findViewById(R.id.startTimeTV);
            groupColourView = itemView.findViewById(R.id.groupColourView);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        @SuppressLint("ResourceAsColor")
        public void setData(Event event, MutableLiveData<ArrayList<Group>> groups) {
            eventNameTV.setText(event.getName());
            startTimeTV.setText(CalendarUtils.formattedTime(event.getStartTime()));
            String groupId = event.getGroupId();
            for (Group group : groups.getValue()) {
                if (group.getId().equals(groupId)) {
                    groupColourView.setBackgroundColor(group.getColours().get(DataBaseAdapter.getEmail()));
                }
            }
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition(), event);
        }
    }
}
