package xyz.hellocraft.arctools;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import xyz.hellocraft.arctools.databinding.ActivityMainBinding;
import xyz.hellocraft.arctools.utils.RootKit;
import xyz.hellocraft.arctools.utils.Tools;
import xyz.hellocraft.arctools.utils.adapter.ScoreAdapter;
import xyz.hellocraft.arctools.utils.data.ScoreData;
import xyz.hellocraft.arctools.utils.data.SongData;
import xyz.hellocraft.arctools.utils.database.ScoreRepo;
import xyz.hellocraft.arctools.utils.database.SongRepo;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";
    private boolean haveRoot = false;
    private SongRepo songRepo;
    private ScoreRepo scoreRepo;
    private RecyclerView recyclerViewSp;
    private ScoreAdapter scoreAdapter;

    Handler cpHandle = new Handler(getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {

            haveRoot = RootKit.haveRoot();
            if (!haveRoot) {
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(getApplicationContext());
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
                Tools.releaseFiles(getApplicationContext(), "database", "/data/user/0/xyz.hellocraft.arctools/databases");
                RootKit.execRootCmdSilent("cp /data/data/moe.low.arc/files/st3 /data/data/xyz.hellocraft.arctools/databases/");
                for (String s : databaseList()) {
                    if (s.equals("arcsong.db")) {
                        songRepo = SongRepo.getINSTANCE(getApplicationContext());
                    }
                    if (s.equals("st3")) {
                        try {
                            scoreRepo = ScoreRepo.getINSTANCE(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                            scoreRepo = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                AlertDialog.Builder normalDialog = new AlertDialog.Builder(getApplicationContext());
                                normalDialog.setTitle("检测到Android 11或更高");
                                normalDialog.setMessage("存档复制失败\n\n请可以手动将st3文件放入data/data/xyz.hellocraft.arctools/databases/文件夹");
                                normalDialog.setPositiveButton("我已知晓",
                                        (dialog, which) -> {
                                            dialog.dismiss();
//                            System.exit(100001);
                                        });
                                normalDialog.setCancelable(false);
                                normalDialog.show();
                            }
                        }
                    }
                }
            }

            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cpHandle.sendEmptyMessage(0);

        binding.buttonReadSongs.setOnClickListener(v -> {
            if (scoreRepo == null) {
                cpHandle.sendEmptyMessage(0);
                return;
            }
            Map<Double, ScoreData> pttMap = new TreeMap<>();
            Map<String, SongData> songDataMap = songRepo.getAllSongs();
            for (ScoreData score : scoreRepo.getAllScores()) {

                String sid = score.getSid();
                SongData songData = songDataMap.get(sid);
                if (songData == null) {
                    Log.w(TAG, "songNotFound: " + score.toString());
                    continue;
                }
                score.setSongData(songData);
                double ptt = Tools.getPtt(score);
                score.setPtt(ptt);
                pttMap.put(ptt, score);

            }
            pttMap = ((TreeMap) pttMap).descendingMap();
            int i = 0;
            int b30ScoreTotal = 0;
            double b30PttTotal = 0.0;
            SongData b30SongHead = new SongData();
            b30SongHead.setName("Best");
            ScoreData b30ScoreHead = new ScoreData();
            b30ScoreHead.setSongData(b30SongHead);
            b30ScoreHead.setDiffType("30");
            List<ScoreData> scoreDataList = new ArrayList<>();
            scoreDataList.add(b30ScoreHead);
            for (Double key : pttMap.keySet()) {

//                if (i>30) break;
                ScoreData scoreData = pttMap.get(key);
                Log.d(TAG, scoreData.getSid() + "-" + scoreData.getDiffType() + "-" + scoreData.getScore() + ":" + key);
                if (i < 30) {
                    b30ScoreTotal += scoreData.getScore();
                    b30PttTotal += scoreData.getPtt();
                }
                i++;
                scoreDataList.add(scoreData);
            }
            scoreDataList.get(0).setScore(b30ScoreTotal / 30);
            scoreDataList.get(0).setPtt(b30PttTotal / 30);
            recyclerViewSp = binding.recyclerViewB30;
            scoreAdapter = new ScoreAdapter(this);
            scoreAdapter.setScoreDataList(scoreDataList);
            recyclerViewSp.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewSp.setAdapter(scoreAdapter);

            //Log.d(TAG,databaseList().toString());
        });
    }


}