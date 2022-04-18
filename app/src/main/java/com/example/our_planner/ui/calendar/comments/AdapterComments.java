package com.example.our_planner.ui.calendar.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.Comment;

import java.util.ArrayList;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.ViewHolderComments>{

    ArrayList<Comment> comments;

    public AdapterComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolderComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_comments,parent,false);
        return new ViewHolderComments(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComments holder, int position) {
        holder.setData(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolderComments extends RecyclerView.ViewHolder {
        private TextView user;
        private TextView date;
        private TextView message;

        public ViewHolderComments(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            date = itemView.findViewById(R.id.date);
            message = itemView.findViewById(R.id.commentMessage);
        }

        public void setData(Comment comment) {
            user.setText(comment.getUser());
            date.setText(comment.getDate());
            message.setText(comment.getMessage());
        }
    }
}
