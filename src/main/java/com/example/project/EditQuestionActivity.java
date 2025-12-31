package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditQuestionActivity extends AppCompatActivity {

    EditText questionET;
    LinearLayout optionsContainer;
    Button addOptionBtn, saveBtn, addImageBtn;
    Spinner correctAnswerSpinner;
    ImageView questionImage;

    ArrayList<EditText> optionETs = new ArrayList<>();
    ArrayList<String> optionsList = new ArrayList<>();

    String imageUriStr = "";
    String roomId = "";
    int qid = -1;

    ActivityResultLauncher<Intent> imageLauncher;

    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> spinnerNumbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        questionET = findViewById(R.id.questionET);
        optionsContainer = findViewById(R.id.previewOptionsContainer);
        addOptionBtn = findViewById(R.id.addOptionBtn);
        saveBtn = findViewById(R.id.saveBtn);
        correctAnswerSpinner = findViewById(R.id.correctAnswerSpinner);
        questionImage = findViewById(R.id.previewQuestionImage);
        addImageBtn = findViewById(R.id.addImageBtn);

        qid = getIntent().getIntExtra("id", -1);
        roomId = getIntent().getStringExtra("roomId");
        QuestionDatabase db = new QuestionDatabase(this);

        spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerNumbers
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        correctAnswerSpinner.setAdapter(spinnerAdapter);

        imageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            imageUriStr = uri.toString();
                            questionImage.setImageURI(uri);
                        }
                    }
                });

        addImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imageLauncher.launch(intent);
        });

        addOptionBtn.setOnClickListener(v -> {
            addOption("");
            updateSpinnerNumbers();
        });

        if (qid == -1) {
            Toast.makeText(this, "Invalid question id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        QuestionModel qm = db.getQuestionById(qid);
        if (qm == null) {
            Toast.makeText(this, "Question not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (roomId == null) {
            roomId = qm.getRoomId();
        }

        questionET.setText(qm.getQuestion());
        imageUriStr = qm.getImageUri();

        if (imageUriStr != null && !imageUriStr.isEmpty()) {
            questionImage.setImageURI(Uri.parse(imageUriStr));
        }

        optionETs.clear();
        optionsContainer.removeAllViews();
        for (String op : qm.getOptions()) {
            addOption(op);
        }

        updateSpinnerNumbers();

        int correctIndex = -1;
        for (int i = 0; i < optionETs.size(); i++) {
            String txt = optionETs.get(i).getText().toString().trim();
            if (txt.equals(qm.getCorrectAnswer())) {
                correctIndex = i;
                break;
            }
        }
        if (correctIndex >= 0 && correctIndex < spinnerNumbers.size()) {
            correctAnswerSpinner.setSelection(correctIndex);
        }

        saveBtn.setText("Update");

        saveBtn.setOnClickListener(v -> {
            String questionText = questionET.getText().toString().trim();
            optionsList.clear();

            for (EditText et : optionETs) {
                String val = et.getText().toString().trim();
                if (!val.isEmpty()) {
                    optionsList.add(val);
                }
            }

            if (questionText.isEmpty() || optionsList.size() < 2) {
                Toast.makeText(this,
                        "Fill question and at least 2 options",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPos = correctAnswerSpinner.getSelectedItemPosition();
            if (selectedPos < 0 || selectedPos >= optionsList.size()) {
                Toast.makeText(this,
                        "Select correct option number",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String correct = optionsList.get(selectedPos);

            db.updateQuestion(qid, roomId, questionText, imageUriStr, optionsList, correct);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void addOption(String value) {
        EditText et = new EditText(this);
        et.setHint("Option " + (optionETs.size() + 1));
        et.setText(value);
        optionsContainer.addView(et);
        optionETs.add(et);
    }

    private void updateSpinnerNumbers() {
        spinnerNumbers.clear();
        for (int i = 0; i < optionETs.size(); i++) {
            spinnerNumbers.add(String.valueOf(i + 1));
        }
        spinnerAdapter.notifyDataSetChanged();
    }
}
