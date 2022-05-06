package com.example.our_planner.ui.calendar.comments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Comment;

import java.util.ArrayList;

public class CommentsViewModel extends AndroidViewModel implements DataBaseAdapter.CommentInterface {
    MutableLiveData<ArrayList<Comment>> comments;

    // COnstruvctor aplication


    public CommentsViewModel(Application application) {
        super(application);
        comments = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.loadComments(this);
    }

    public MutableLiveData<ArrayList<Comment>> getComments() {
        return comments;
    }

    @Override
    public void addComment(Comment comment) {
        comments.getValue().add(comment);
        comments.setValue(comments.getValue());
    }
}
