package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ResultDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "ResultDB";
    private static final int DB_VERSION = 1;

    public ResultDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE results (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "roomId TEXT," +
                        "studentName TEXT," +
                        "score INTEGER," +
                        "total INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS results");
        onCreate(db);
    }

    public void insertResult(String roomId, String studentName, int score, int total) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("roomId", roomId);
        cv.put("studentName", studentName);
        cv.put("score", score);
        cv.put("total", total);
        db.insert("results", null, cv);
    }

    public ArrayList<ResultModel> getResultsByRoomId(String roomId) {
        ArrayList<ResultModel> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM results WHERE roomId=?",
                new String[]{roomId});

        if (c.moveToFirst()) {
            do {
                list.add(new ResultModel(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getInt(3),
                        c.getInt(4)
                ));
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }
}
