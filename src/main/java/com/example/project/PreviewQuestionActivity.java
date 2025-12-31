package com.example.project;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class PreviewQuestionActivity extends AppCompatActivity {

    TextView questionText;
    ImageView questionImage;
    LinearLayout optionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_question);

        questionText = findViewById(R.id.previewQuestionText);
        questionImage = findViewById(R.id.previewQuestionImage);
        optionsContainer = findViewById(R.id.previewOptionsContainer);

        int id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            finish();
            return;
        }

        QuestionDatabase db = new QuestionDatabase(this);
        QuestionModel qm = db.getQuestionById(id);
        if (qm == null) {
            finish();
            return;
        }

        questionText.setText(qm.getQuestion());

        String img = qm.getImageUri();
        if (img != null && !img.isEmpty()) {
            try {
                questionImage.setImageURI(Uri.parse(img));
            } catch (Exception e) {
            }
        }

        optionsContainer.removeAllViews();
        for (String opt : qm.getOptions()) {
            RadioButton rb = new RadioButton(this);
            rb.setText(opt);
            rb.setEnabled(false);
            optionsContainer.addView(rb);
        }
    }
}
