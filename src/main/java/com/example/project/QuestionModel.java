package com.example.project;

import java.util.ArrayList;

public class QuestionModel {

    private int id;
    private String roomId;
    private String question;
    private String imageUri;
    private ArrayList<String> options;
    private String correctAnswer;

    public QuestionModel(int id, String roomId, String question,
                         String imageUri, ArrayList<String> options,
                         String correctAnswer) {
        this.id = id;
        this.roomId = roomId;
        this.question = question;
        this.imageUri = imageUri;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }
    public String getRoomId() {
        return roomId;
    }
    public String getQuestion() {
        return question;
    }
    public String getImageUri() {
        return imageUri;
    }
    public ArrayList<String> getOptions() {
        return options;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
