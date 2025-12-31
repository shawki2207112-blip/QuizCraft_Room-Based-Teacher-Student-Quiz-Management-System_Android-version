package com.example.project;

public class ResultModel {
    private int id;
    private String roomId;
    private String studentName;
    private int score;
    private int total;

    public ResultModel(int id, String roomId, String studentName, int score, int total) {
        this.id = id;
        this.roomId = roomId;
        this.studentName = studentName;
        this.score = score;
        this.total = total;
    }

    public int getId() {
        return id;
    }
    public String getRoomId() {
        return roomId;
    }
    public String getStudentName() {
        return studentName;
    }
    public int getScore() {
        return score;
    }
    public int getTotal() {
        return total;
    }
}
