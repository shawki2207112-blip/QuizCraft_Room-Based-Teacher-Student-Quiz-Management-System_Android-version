package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ResultAdapter adapter;
    ResultDatabase db;
    ArrayList<ResultModel> results;
    String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        recyclerView = findViewById(R.id.resultsRecyclerView);
        db = new ResultDatabase(this);
        roomId = getIntent().getStringExtra("roomId");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (roomId == null) {
            results = new ArrayList<>();
        } else {
            results = db.getResultsByRoomId(roomId);
        }
        adapter = new ResultAdapter(this, results);
        recyclerView.setAdapter(adapter);
    }
}
