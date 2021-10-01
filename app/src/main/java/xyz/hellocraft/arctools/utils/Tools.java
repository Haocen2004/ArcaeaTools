package xyz.hellocraft.arctools.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import xyz.hellocraft.arctools.utils.data.ScoreData;
import xyz.hellocraft.arctools.utils.data.SongData;

public class Tools {
    private static final String TAG = "Tools";


    public static void openApp(String packageName, Activity activity){
        PackageManager manager = activity.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(packageName);

        List<ResolveInfo> apps = manager.queryIntentActivities(intent,0);
        if (apps.size() >0) {
            ResolveInfo ri = apps.get(0);

            String pName = ri.activityInfo.packageName;
            String cName = ri.activityInfo.name;
            ComponentName cn = new ComponentName(pName,cName);
            intent.setComponent(cn);
            activity.startActivity(intent);
        } else {
            ToastUtils.show("找不到包名！");
        }

    }

    public static double getPtt(ScoreData scoreData) {
        SongData songData = scoreData.getSongData();
//        Log.d(TAG, "getPtt: "+scoreData.toString());
//        Log.d(TAG, "getPtt: "+songData.toString());
        int rating = songData.getRating(scoreData.getDifficulty());
        int score = scoreData.getScore();
        double ptt;
        if (score < 9800000) {
            ptt = (double) rating / 10.0 + (double) (score - 9500000) / 300000;
        } else if (score < 10000000) {
            ptt = (double) rating / 10.0 + 1.0 + (double) (score - 9800000) / 200000;
        } else {
            ptt = (double) rating / 10.0 + 2.0;
        }
        if (ptt <= 0.0) {
            ptt = 0.0;
        }
        return ptt;

    }

    public static void releaseFiles(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    releaseFiles(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {
                File newFile = new File(newPath);
                if (newFile.exists() && newPath.contains("st3")) {
                    return;
                }
                Log.d("releaseFiles", "copy " + oldPath + " to " + newPath);
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(newFile);
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
