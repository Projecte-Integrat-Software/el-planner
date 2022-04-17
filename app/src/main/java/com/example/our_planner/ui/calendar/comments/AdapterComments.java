package com.example.our_planner.ui.calendar.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.ChatMessage;

import java.util.ArrayList;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolderComments>{

    ArrayList<ChatMessage> chatMessages;

    public AdapterComments(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ViewHolderComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_messages,parent,false);
        return new ViewHolderComments(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComments holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ViewHolderComments extends RecyclerView.ViewHolder {
        private TextView user;
        private TextView date;
        private TextView message;

        public ViewHolderComments(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            date = itemView.findViewById(R.id.date);
            message = itemView.findViewById(R.id.text);
        }

        public void setData(ChatMessage chatMessage) {
            user.setText(chatMessage.getUser());
            date.setText(chatMessage.getDate());
            message.setText(chatMessage.getMessage());
        }
    }
}
