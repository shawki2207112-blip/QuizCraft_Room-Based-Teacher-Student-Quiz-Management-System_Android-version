package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class loginT extends AppCompatActivity {
    Button logint;
    TextInputEditText name,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_t);
        logint=findViewById(R.id.LoginT);
        name=findViewById(R.id.nameT);
        logint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString().trim();

                if (n.isEmpty()) {
                    name.setError("Name required");
                    name.requestFocus();
                    return;
                }


                Intent intent= new Intent(loginT.this,DashboardT.class);
                intent.putExtra("Tname",n);
                startActivity(intent);
            }
        });
    }
}