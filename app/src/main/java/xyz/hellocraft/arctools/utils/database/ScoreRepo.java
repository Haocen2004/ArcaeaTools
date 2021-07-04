package xyz.hellocraft.arctools.utils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.hellocraft.arctools.utils.data.ScoreData;

public class ScoreRepo {
    private Context mContext;
    private SQLiteDatabase db;
    private final String TAG = "ScoreRepo";
    private List<ScoreData> scoreDataList;


    private static ScoreRepo INSTANCE;

    public static ScoreRepo getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ScoreRepo(context);
        }
        INSTANCE.refreshDB();
        return INSTANCE;
    }

    public void refreshDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext, "st3", null, 1);
        db = dataBaseHelper.getReadableDatabase();
    }


    public ScoreRepo(Context context) {
        mContext = context;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context, "st3", null, 1);
        db = dataBaseHelper.getReadableDatabase();
    }

    public List<ScoreData> getAllScores() {
        Log.d(TAG, "Reading Scores From Database...");
        scoreDataList = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from scores", null);

            while (cursor.moveToNext()) {
                ScoreData scoreData = new ScoreData();
                scoreData.setSid(cursor.getString(cursor.getColumnIndex("songId")));
                scoreData.setScore(cursor.getInt((cursor.getColumnIndex("score"))));
                scoreData.setShinyPerfectCount(cursor.getInt((cursor.getColumnIndex("shinyPerfectCount"))));
                scoreData.setPerfectCount(cursor.getInt((cursor.getColumnIndex("perfectCount"))));
                scoreData.setFarCount(cursor.getInt((cursor.getColumnIndex("nearCount"))));
                scoreData.setLostCount(cursor.getInt((cursor.getColumnIndex("missCount"))));
                scoreData.setDifficulty(cursor.getInt((cursor.getColumnIndex("songDifficulty"))));
                scoreDataList.add(scoreData);

            }
            Log.d(TAG, "Scores Readied, Total " + scoreDataList.size() + " record(s).");
            cursor.close();
        } else {
            throw new SQLiteCantOpenDatabaseException();
        }
        return scoreDataList;
    }
}
