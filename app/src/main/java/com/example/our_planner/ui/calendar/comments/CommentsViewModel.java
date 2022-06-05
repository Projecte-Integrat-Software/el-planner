package com.example.our_planner.ui.calendar.comments;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.model.Comment;

import java.util.ArrayList;

public class CommentsViewModel extends AndroidViewModel implements DataBaseAdapter.CommentInterface {
    MutableLiveData<ArrayList<Comment>> comments;
    MutableLiveData<String> idEvent;

    public CommentsViewModel(Application application) {
        super(application);
        comments = new MutableLiveData<>(new ArrayList<>());
        idEvent = new MutableLiveData<>();
    }

    public void setIdEvent(String idEvent) {
        this.idEvent.setValue(idEvent);
        DataBaseAdapter.loadComments(this, idEvent);
    }

    public MutableLiveData<ArrayList<Comment>> getComments() {
        return comments;
    }

    @Override
    public void addComment(Comment comment) {
        comments.getValue().add(comment);
        comments.setValue(comments.getValue());
    }

    public void postComment(String messageToPost) {
        DataBaseAdapter.postComment(idEvent.getValue(), messageToPost);
    }
}
