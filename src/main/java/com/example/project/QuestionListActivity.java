package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    QuestionAdapter adapter;
    QuestionDatabase db;
    ArrayList<QuestionModel> questions;
    String mode;
    String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        recyclerView = findViewById(R.id.recyclerView);
        db = new QuestionDatabase(this);

        mode = getIntent().getStringExtra("mode");
        if (mode == null) mode = "preview";

        roomId = getIntent().getStringExtra("roomId");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (roomId == null || roomId.isEmpty()) {
            questions = db.getAllQuestions();
        } else {
            questions = db.getQuestionsByRoomId(roomId);
        }
        adapter = new QuestionAdapter(this, questions, mode);
        recyclerView.setAdapter(adapter);
    }
}
