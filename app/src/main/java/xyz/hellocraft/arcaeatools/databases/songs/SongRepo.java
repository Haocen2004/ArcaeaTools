package xyz.hellocraft.arcaeatools.databases.songs;

import android.content.Context;

import java.util.List;

public class SongRepo {
    private final Context context;
    private final SongDao songDao;
    private List<SongData> songDataList;


    public SongRepo(Context context) {
        this.context = context;
        SongDatabase songDatabase = SongDatabase.getDatabase(context.getApplicationContext());
        songDao = songDatabase.getSongDao();
        songDataList = songDao.getAllSongs();
    }

    public SongDao getSongDao() {
        return songDao;
    }

    public List<SongData> getSongDataList() {
        return songDataList;
    }

}
