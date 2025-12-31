package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardT extends AppCompatActivity {

    TextView tName;
    EditText roomIdTeacherET;
    Button btnCreate, btnUpdate, btnDelete, btnPreview, btnResults;
    String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_t);

        tName = findViewById(R.id.tname);
        roomIdTeacherET = findViewById(R.id.roomIdTeacherET);
        btnCreate = findViewById(R.id.button3);
        btnUpdate = findViewById(R.id.button2);
        btnDelete = findViewById(R.id.buttonDelete);
        btnPreview = findViewById(R.id.button);
        btnResults = findViewById(R.id.buttonResults);
        String teacherName = getIntent().getStringExtra("Tname");
        if (teacherName != null && !teacherName.isEmpty()) {
            tName.setText("Welcome, " + teacherName);
        } else {
            tName.setText("Welcome, Teacher");
        }

        if(teacherName!=null) tName.setText(teacherName);

        btnCreate.setOnClickListener(v -> {
            roomId = roomIdTeacherET.getText().toString().trim();
            if (roomId.isEmpty()) {
                Toast.makeText(this, "Enter room ID first", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(DashboardT.this, CreateQuestionActivity.class);
            i.putExtra("roomId", roomId);
            startActivity(i);
        });

        btnUpdate.setOnClickListener(v -> {
            roomId = roomIdTeacherET.getText().toString().trim();
            if (roomId.isEmpty()) {
                Toast.makeText(this, "Enter room ID first", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(DashboardT.this, QuestionListActivity.class);
            i.putExtra("mode","update");
            i.putExtra("roomId", roomId);
            startActivity(i);
        });

        btnDelete.setOnClickListener(v -> {
            roomId = roomIdTeacherET.getText().toString().trim();
            if (roomId.isEmpty()) {
                Toast.makeText(this, "Enter room ID first", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(DashboardT.this, QuestionListActivity.class);
            i.putExtra("mode","delete");
            i.putExtra("roomId", roomId);
            startActivity(i);
        });

        btnPreview.setOnClickListener(v -> {
            roomId = roomIdTeacherET.getText().toString().trim();
            if (roomId.isEmpty()) {
                Toast.makeText(this, "Enter room ID first", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(DashboardT.this, QuestionListActivity.class);
            i.putExtra("mode","preview");
            i.putExtra("roomId", roomId);
            startActivity(i);
        });

        btnResults.setOnClickListener(v -> {
            roomId = roomIdTeacherET.getText().toString().trim();
            if (roomId.isEmpty()) {
                Toast.makeText(this, "Enter room ID first", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(DashboardT.this, ResultListActivity.class);
            i.putExtra("roomId", roomId);
            startActivity(i);
        });
    }
}
