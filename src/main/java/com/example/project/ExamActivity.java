package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class ExamActivity extends AppCompatActivity {

    TextView questionText;
    ImageView examQuestionImage;
    RadioGroup optionsGroup;
    Button nextBtn;

    ArrayList<QuestionModel> questions;
    int index = 0;
    int score = 0;

    String roomId;
    String studentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        examQuestionImage = findViewById(R.id.examQuestionImage);
        questionText = findViewById(R.id.examQuestionText);
        optionsGroup = findViewById(R.id.examOptionsGroup);
        nextBtn = findViewById(R.id.examNextBtn);

        roomId = getIntent().getStringExtra("roomId");
        studentName = getIntent().getStringExtra("studentName");

        QuestionDatabase db = new QuestionDatabase(this);
        if (roomId == null || roomId.isEmpty()) {
            questions = db.getAllQuestions();
        } else {
            questions = db.getQuestionsByRoomId(roomId);
        }

        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "No questions for this room", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Collections.shuffle(questions);
        loadQuestion();

        nextBtn.setOnClickListener(v -> checkAnswerAndNext());
    }

    private void loadQuestion() {
        if (index >= questions.size()) {
            ResultDatabase rdb = new ResultDatabase(this);
            int total = questions.size();
            String nameToSave = (studentName == null || studentName.isEmpty())
                    ? "Student"
                    : studentName;
            rdb.insertResult(roomId, nameToSave, score, total);

            Intent i = new Intent(this, ScoreActivity.class);
            i.putExtra("score", score);
            i.putExtra("total", total);
            i.putExtra("roomId", roomId);
            i.putExtra("studentName", nameToSave);
            startActivity(i);

            finish();
            return;
        }

        QuestionModel qm = questions.get(index);
        questionText.setText(qm.getQuestion());

        String img = qm.getImageUri();
        if (img != null && !img.isEmpty()) {
            try {
                Uri uri = Uri.parse(img);
                examQuestionImage.setImageURI(uri);
                examQuestionImage.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                examQuestionImage.setVisibility(View.GONE);
            }
        } else {
            examQuestionImage.setVisibility(View.GONE);
        }

        optionsGroup.removeAllViews();
        for (String opt : qm.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rb.setTextSize(18);
            optionsGroup.addView(rb);
        }

        nextBtn.setText(index == questions.size() - 1 ? "Finish" : "Next");
    }


    private void checkAnswerAndNext() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selected = findViewById(selectedId);
            String selectedText = selected.getText().toString();
            if (selectedText.equals(questions.get(index).getCorrectAnswer())) {
                score++;
            }
        }

        index++;
        loadQuestion();
    }
}
