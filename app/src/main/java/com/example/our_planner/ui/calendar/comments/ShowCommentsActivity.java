package com.example.our_planner.ui.calendar.comments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.DataBaseAdapter;
import com.example.our_planner.R;
import com.example.our_planner.model.Comment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShowCommentsActivity extends AppCompatActivity {

    RecyclerView recyclerViewComments;
    EditText message;
    FloatingActionButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        message = findViewById(R.id.message);
        sendBtn = findViewById(R.id.sendBtn);


        ShowCommentsViewModel viewModel = new ViewModelProvider(this).get(ShowCommentsViewModel.class);
        MutableLiveData<ArrayList<Comment>> comments = viewModel.getComments();
        AdapterComments adapterComments = new AdapterComments(comments.getValue());
        recyclerViewComments.setAdapter(adapterComments);

        comments.observe(this, new Observer<ArrayList<Comment>>(){
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                adapterComments.notifyDataSetChanged();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToPost = message.getText().toString();
                if (!messageToPost.equals("")){
                    DataBaseAdapter.postComment(messageToPost);
                    message.setText("");
                }
            }
        });
    }


}
