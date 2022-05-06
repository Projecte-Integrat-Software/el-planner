package com.example.our_planner.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Event;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolderEvents> {

    private final ArrayList<Event> events;
    private final Context context;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolderEvents onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false);
        return new ViewHolderEvents(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEvents holder, int position) {
        Event e = events.get(position);
        holder.setData(e);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolderEvents extends RecyclerView.ViewHolder {

        private final TextView eventCellTV;

        public ViewHolderEvents(@NonNull View itemView) {
            super(itemView);
            eventCellTV = itemView.findViewById(R.id.eventCellTV);
        }

        public void setData(Event event) {
            String eventTitle = event.getName() + " " + CalendarUtils.formattedTime(event.getStartTime());
            eventCellTV.setText(eventTitle);
        }
    }
}
