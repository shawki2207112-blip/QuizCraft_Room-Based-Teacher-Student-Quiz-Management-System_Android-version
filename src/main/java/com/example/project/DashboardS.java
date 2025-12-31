package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardS extends AppCompatActivity {

    EditText roomIdET, studentNameET;
    Button startExamBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_s);

        roomIdET = findViewById(R.id.roomIdET);
        studentNameET = findViewById(R.id.studentNameET);
        startExamBtn = findViewById(R.id.startExamBtn);

        startExamBtn.setOnClickListener(v -> {
            String roomId = roomIdET.getText().toString().trim();
            String studentName = studentNameET.getText().toString().trim();
            if(roomId.isEmpty() || studentName.isEmpty()){
                Toast.makeText(this,"Enter room id and name",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(DashboardS.this, ExamActivity.class);
            i.putExtra("roomId", roomId);
            i.putExtra("studentName", studentName);
            startActivity(i);
        });
    }
}
