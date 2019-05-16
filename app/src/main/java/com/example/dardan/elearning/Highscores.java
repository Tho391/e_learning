package com.example.dardan.elearning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.dardan.elearning.MySQLiteHelper.*;

/**
 * Created by Dardan on 11/17/2016.
 */

public class Highscores {

    private static SQLiteDatabase database;
    private static MySQLiteHelper dbHelper;
    private static final String[] all_columns = {COLUMN_ID,
            COLUMN_TITLE, COLUMN_IMAGE, COLUMN_HIGHSCORE, COLUMN_COLOR, COLUMN_THEME};

    public static void open(Context context) throws SQLException {
        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static void close() {
        dbHelper.close();
    }

    public static List<Integer> getAllHighscores() {
        Cursor cursor = database.query(TABLE_CATEGORY,
                all_columns, null, null, null, null, null);
        cursor.moveToFirst();
        List<Integer> highScores = new ArrayList<>();
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            highScores.add(Integer.parseInt(cursor.getString(3)));
        }
        cursor.close();
        return highScores;
    }

    public static int getHighscore(String column) {
        String[] col = {column};
        Cursor cursor = database.query(TABLE_CATEGORY, col, null, null, null, null, null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }

    public static boolean setHighscore(String column, int newScore) {
        boolean result = false;
        String[] col = {column};
        Cursor cursor = database.query(TABLE_CATEGORY, col, null, null, null, null, null);
        cursor.moveToFirst();
        int oldScore = cursor.getInt(0);
        if (newScore > oldScore) {
            ContentValues cv = new ContentValues();
            cv.put(column, newScore);
            database.update(TABLE_CATEGORY, cv, COLUMN_ID + "=1", null);
            result=true;
        }
        cursor.close();
        return result;
    }

}
