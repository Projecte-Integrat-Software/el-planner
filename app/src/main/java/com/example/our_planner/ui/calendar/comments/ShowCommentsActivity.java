package com.example.our_planner.ui.calendar.comments;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.our_planner.R;
import com.example.our_planner.model.ChatMessage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShowCommentsActivity extends AppCompatActivity {

    RecyclerView recyclerViewComments;
    EditText message;
    FloatingActionButton sendBtn;
    ArrayList<ChatMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        message = findViewById(R.id.message);
        sendBtn = findViewById(R.id.sendBtn);

        messages = new ArrayList<>();
        messages.add(new ChatMessage("Hi!","Miquel Sala"));
        messages.add(new ChatMessage("Hey!","Pol Gabaldon"));

        AdapterComments adapterComments = new AdapterComments(messages);
        recyclerViewComments.setAdapter(adapterComments);
    }
}
