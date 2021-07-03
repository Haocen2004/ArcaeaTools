package xyz.hellocraft.arctools.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import xyz.hellocraft.arctools.utils.data.ScoreData;
import xyz.hellocraft.arctools.utils.data.SongData;

public class Tools {

    public static float getPtt(ScoreData scoreData, SongData songData) {
        int rating = 0;
        int score = scoreData.getScore();
        float ptt;
        switch (scoreData.getDifficulty()) {
            case 0:
                rating = songData.getRating_pst();
            case 1:
                rating = songData.getRating_prs();
            case 2:
                rating = songData.getRating_ftr();
            case 3:
                rating = songData.getRating_byd();
        }
        if (score < 9800000) {
            ptt = rating + (score - 9500000) / 300000;
        } else if (score < 10000000) {
            ptt = rating + 1 + (score - 9800000) / 200000;
        } else {
            ptt = rating + 2;
        }

        return ptt;

    }

    public static void releaseFiles(Context context, String oldPath, String newPath) {
        try {
            Log.d("releaseFiles","copy "+oldPath+" to "+newPath);
            String fileNames[] = context.getAssets().list(oldPath);
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    releaseFiles(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
