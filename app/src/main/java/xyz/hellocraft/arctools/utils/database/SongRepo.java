package xyz.hellocraft.arctools.utils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import xyz.hellocraft.arctools.utils.data.SongData;

public class SongRepo {

    private Context mContext;
    private SQLiteDatabase db;
    private final String TAG="SongRepo";
    private Map<String,SongData> songDataMap;


    private static SongRepo INSTANCE;

    public static SongRepo getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SongRepo(context);
        }
        return INSTANCE;
    }


    public SongRepo(Context context) {
        mContext = context;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context,"arcsong.db",null,1);
        db = dataBaseHelper.getReadableDatabase();
    }

    public Map<String,SongData> getAllSongs(){

        if (songDataMap == null) {
            Log.d(TAG,"Reading Song List From Database...");
            songDataMap = new HashMap<>();
            if(db.isOpen()) {
                Cursor cursor = db.rawQuery("select * from songs",null);
                while (cursor.moveToNext()){
                    SongData songData = new SongData();
                    String sid = cursor.getString(cursor.getColumnIndex("sid"));
                    songData.setSid(sid);
                    songData.setName(cursor.getString(cursor.getColumnIndex("name_en")));
                    songData.setRating_pst(cursor.getInt(cursor.getColumnIndex("rating_pst")));
                    songData.setRating_prs(cursor.getInt(cursor.getColumnIndex("rating_prs")));
                    songData.setRating_ftr(cursor.getInt(cursor.getColumnIndex("rating_ftr")));
                    songData.setRating_byd(cursor.getInt(cursor.getColumnIndex("rating_byn")));

                    songDataMap.put(sid,songData);
                }

                Log.d(TAG,"Song List Readied, Total "+ songDataMap.size() +" song(s).");
                cursor.close();
            }
        }

        return songDataMap;
    }
}
