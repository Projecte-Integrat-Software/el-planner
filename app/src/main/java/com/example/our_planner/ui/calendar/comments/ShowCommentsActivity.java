package com.example.our_planner.ui.calendar.comments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
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
    ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        message = findViewById(R.id.message);
        sendBtn = findViewById(R.id.sendBtn);

        comments = new ArrayList<>();

        AdapterComments adapterComments = new AdapterComments(comments);
        recyclerViewComments.setAdapter(adapterComments);
        // Show messages in database or that will be published
        DataBaseAdapter.getCommentsDatabase(comments, adapterComments);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToPost = message.getText().toString();
                if (messageToPost != ""){
                    DataBaseAdapter.postComment(messageToPost);
                    message.setText("");
                }
            }
        });

    }
}
