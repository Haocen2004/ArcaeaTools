package xyz.hellocraft.arctools;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.TreeMap;

import xyz.hellocraft.arctools.utils.Tools;
import xyz.hellocraft.arctools.utils.RootKit;
import xyz.hellocraft.arctools.utils.data.ScoreData;
import xyz.hellocraft.arctools.utils.data.SongData;
import xyz.hellocraft.arctools.utils.database.ScoreRepo;
import xyz.hellocraft.arctools.utils.database.SongRepo;
import xyz.hellocraft.arctools.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";
    private boolean haveRoot = false;
    private SongRepo songRepo;
    private ScoreRepo scoreRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Thread(()->{
            haveRoot = RootKit.haveRoot();
            if(!haveRoot){
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
                normalDialog.setTitle("设备没有Root");
                normalDialog.setMessage("本app依赖root权限来读取arcaea存档\n\n你也可以手动将st3文件放入data/data/xyz.hellocraft.arctools/databases/文件夹");
                normalDialog.setPositiveButton("我已知晓",
                        (dialog, which) -> {
                            dialog.dismiss();
//                            System.exit(100001);
                        });
                normalDialog.setCancelable(false);
                normalDialog.show();
            } else {
                Tools.releaseFiles(getApplicationContext(),"database","/data/user/0/xyz.hellocraft.arctools/databases");
//                RootKit.execRootCmd("cp /data/data/moe.low.arc/files/st3 /data/data/xyz.hellocraft.arctools/databases/");
                for (String s : databaseList()) {
                    if (s.equals("arcsong.db")) {
                        songRepo = SongRepo.getINSTANCE(getApplicationContext());
                    }
                    if (s.equals("st3")) {
                        scoreRepo = ScoreRepo.getINSTANCE(getApplicationContext());
                    }
                }
            }
        }).start();



        binding.buttonReadSongs.setOnClickListener(v -> {
            boolean hasArcData = false;
            for (String s : databaseList()) {
                if (s.equals("st3")) {
                    hasArcData = true;
                }
                Log.d(TAG,s);
            }
            if (!hasArcData) {
                if(haveRoot) {
                    Toast.makeText(this,"正在尝试获取Arcaea数据\n请稍后再试",Toast.LENGTH_SHORT).show();
                    int ret = RootKit.execRootCmdSilent("cp /data/data/moe.low.arc/files/st3 /data/data/xyz.hellocraft.arctools/databases/");
                    if (ret != -1) {
                        Log.i(TAG, "Arc data copied.");
                        Toast.makeText(this,"数据获取成功",Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Arc data not found.");
                        Toast.makeText(this,"数据获取失败",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
                    normalDialog.setTitle("设备没有Root");
                    normalDialog.setMessage("本app依赖root权限来读取arcaea存档\n\n你也可以手动将st3文件放入data/data/xyz.hellocraft.arctools/database文件夹");
                    normalDialog.setPositiveButton("我已知晓",
                            (dialog, which) -> {
                                dialog.dismiss();
//                            System.exit(100001);
                            });
                    normalDialog.setCancelable(false);
                    normalDialog.show();
                }
                return;
            }
            Map<Float,ScoreData> pttMap = new TreeMap<>();
            Map<String, SongData> songDataMap = songRepo.getAllSongs();
            for (ScoreData score : scoreRepo.getAllScores()) {

                String sid = score.getSid();
                SongData songData = songDataMap.get("sid");
                float ptt = Tools.getPtt(score,songData);
                score.setPtt(ptt);
                pttMap.put(ptt,score);

            }
            int i =0;
            for (Float aFloat : pttMap.keySet()) {
                if (i>30) break;
                Log.d(TAG,pttMap.get(aFloat).getSid()+":"+aFloat);
                i++;
            }


            //Log.d(TAG,databaseList().toString());
        });
    }


}