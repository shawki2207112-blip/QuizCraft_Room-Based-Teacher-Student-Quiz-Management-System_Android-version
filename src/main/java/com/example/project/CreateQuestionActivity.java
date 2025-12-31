package com.example.project;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class CreateQuestionActivity extends AppCompatActivity {

    private static final int REQ_READ_MEDIA = 1001;
    EditText questionET;
    LinearLayout optionsContainer;
    Button addOptionBtn, saveBtn, addImageBtn;
    Spinner correctAnswerSpinner;
    ImageView questionImage;

    ArrayList<EditText> optionETs = new ArrayList<>();
    ArrayList<String> optionsList = new ArrayList<>();

    String imageUriStr = "";
    String roomId = "";

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

        roomId = getIntent().getStringExtra("roomId");
        if (roomId == null) roomId = "";

        spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerNumbers
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        correctAnswerSpinner.setAdapter(spinnerAdapter);

        addOption();
        addOption();

        addOptionBtn.setOnClickListener(v -> addOption());

        imageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        if (uri != null) {

                            int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                            try {
                                getContentResolver()
                                        .takePersistableUriPermission(uri, takeFlags);
                            } catch (SecurityException ignored) { }

                            imageUriStr = uri.toString();
                            try {
                                questionImage.setImageURI(uri);
                                questionImage.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                questionImage.setVisibility(View.GONE);
                            }
                        }
                    }
                });


        addImageBtn.setOnClickListener(v -> {
            if (!hasImagePermission()) {
                requestImagePermission();
            } else {
                pickImage();
            }
        });

        saveBtn.setOnClickListener(v -> saveQuestion());
    }

    private boolean hasImagePermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestImagePermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQ_READ_MEDIA
            );
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQ_READ_MEDIA
            );
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        );
        imageLauncher.launch(intent);
    }


    private void addOption() {
        EditText et = new EditText(this);
        et.setHint("Option " + (optionETs.size() + 1));
        optionsContainer.addView(et);
        optionETs.add(et);
        updateSpinnerNumbers();
    }

    private void updateSpinnerNumbers() {
        spinnerNumbers.clear();
        for (int i = 0; i < optionETs.size(); i++) {
            spinnerNumbers.add(String.valueOf(i + 1));
        }
        spinnerAdapter.notifyDataSetChanged();
    }

    private void saveQuestion() {
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

        QuestionDatabase db = new QuestionDatabase(this);
        db.insertQuestion(roomId, questionText, imageUriStr, optionsList, correct);

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_READ_MEDIA) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(this,
                        "Permission denied, cannot pick image",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
