package xyz.hellocraft.arcaeatools.databases.songs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SongDao {
    @Insert
    void insertSongs(SongData... SongsData);

    @Update
    void updateSongs(SongData... SongsData);

    @Query("DELETE FROM SONGS")
    void deleteAllSongs();

    @Query("SELECT * FROM SONGS ORDER BY ID DESC")
    List<SongData> getAllSongs();
}
