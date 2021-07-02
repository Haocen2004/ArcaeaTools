package xyz.hellocraft.arcaeatools.databases.songs;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SongData.class}, version = 1, exportSchema = false)
public abstract class SongDatabase extends RoomDatabase {
    private static SongDatabase INSTANCE;

    static synchronized SongDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SongDatabase.class, "arcSongs")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract SongDao getSongDao();
}
