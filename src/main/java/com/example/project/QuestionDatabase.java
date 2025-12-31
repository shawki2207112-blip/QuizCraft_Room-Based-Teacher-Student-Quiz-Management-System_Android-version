package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestionDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "QuestionDB";
    private static final int DB_VERSION = 2;

    public QuestionDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE questions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "roomId TEXT," +
                        "question TEXT," +
                        "imageUri TEXT," +
                        "options TEXT," +
                        "correctAnswer TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        onCreate(db);
    }

    public void insertQuestion(String roomId, String question, String imageUri,
                               ArrayList<String> options, String correctAnswer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("roomId", roomId);
        cv.put("question", question);
        cv.put("imageUri", imageUri);
        cv.put("options", String.join("|", options));
        cv.put("correctAnswer", correctAnswer);
        db.insert("questions", null, cv);
    }

    public ArrayList<QuestionModel> getAllQuestions() {
        return getQuestionsByRoomId(null);
    }

    public ArrayList<QuestionModel> getQuestionsByRoomId(String roomId) {
        ArrayList<QuestionModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c;
        if (roomId == null) {
            c = db.rawQuery("SELECT * FROM questions", null);
        } else {
            c = db.rawQuery("SELECT * FROM questions WHERE roomId=?",
                    new String[]{roomId});
        }

        if (c.moveToFirst()) {
            do {
                String optionsStr = c.getString(4);
                ArrayList<String> options =
                        new ArrayList<>(Arrays.asList(optionsStr.split("\\|")));

                list.add(new QuestionModel(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        options,
                        c.getString(5)
                ));
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }

    public QuestionModel getQuestionById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM questions WHERE id=?",
                new String[]{String.valueOf(id)});

        if (c.moveToFirst()) {
            String optionsStr = c.getString(4);
            ArrayList<String> options =
                    new ArrayList<>(Arrays.asList(optionsStr.split("\\|")));
            QuestionModel qm = new QuestionModel(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    options,
                    c.getString(5)
            );
            c.close();
            return qm;
        }
        c.close();
        return null;
    }

    public void updateQuestion(int id, String roomId, String question, String imageUri,
                               ArrayList<String> options, String correctAnswer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("roomId", roomId);
        cv.put("question", question);
        cv.put("imageUri", imageUri);
        cv.put("options", String.join("|", options));
        cv.put("correctAnswer", correctAnswer);
        db.update("questions", cv, "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteQuestion(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("questions", "id=?", new String[]{String.valueOf(id)});
    }
}
