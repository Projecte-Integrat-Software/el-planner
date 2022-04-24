package com.example.our_planner.ui.calendar.comments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Comment;

import java.util.ArrayList;

public class ShowCommentsViewModel extends ViewModel {
    static MutableLiveData<ArrayList<Comment>> comments;

    public static MutableLiveData<ArrayList<Comment>> getComments() {
        if (comments==null) comments = new MutableLiveData<>(new ArrayList<>());
        DataBaseAdapter.loadComments(comments);
        return comments;
    }

    public static void addComment(Comment comment) {
        comments.getValue().add(comment);
    }
}
