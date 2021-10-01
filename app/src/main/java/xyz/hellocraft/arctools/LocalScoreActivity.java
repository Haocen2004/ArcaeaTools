package xyz.hellocraft.arctools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import xyz.hellocraft.arctools.databinding.ActivityLocalScoreBinding;
import xyz.hellocraft.arctools.utils.RootKit;
import xyz.hellocraft.arctools.utils.Tools;
import xyz.hellocraft.arctools.utils.adapter.ScoreAdapter;
import xyz.hellocraft.arctools.utils.data.ScoreData;
import xyz.hellocraft.arctools.utils.data.SongData;
import xyz.hellocraft.arctools.utils.database.ScoreRepo;
import xyz.hellocraft.arctools.utils.database.SongRepo;

public class LocalScoreActivity extends AppCompatActivity {

    private ActivityLocalScoreBinding binding;
    private final String TAG = "LocalScoreActivity";
    private SongRepo songRepo;
    private ScoreRepo scoreRepo;
    private RecyclerView recyclerViewSp;
    private ScoreAdapter scoreAdapter;
    private Activity activity;

    @SuppressLint("HandlerLeak")
    Handler cpHandle = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean haveRoot = RootKit.haveRoot();
            if (!haveRoot) {
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(activity);
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
                RootKit.execRootCmdSilent("cp -f /data/data/moe.low.arc/files/st3 /data/data/xyz.hellocraft.arctools/databases/");
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
                                AlertDialog.Builder normalDialog = new AlertDialog.Builder(activity);
                                normalDialog.setTitle("检测到Android 11或更高");
                                normalDialog.setMessage("存档可能无法复制\n\n如果无法正常读取分数\n请手动将 st3 文件放入 data/data/xyz.hellocraft.arctools/databases/ 文件夹");
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
        this.activity = this;
        binding = ActivityLocalScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cpHandle.sendEmptyMessage(0);
        binding.buttonReadSongs.setOnClickListener(v -> {
            if (scoreRepo == null) {
                cpHandle.sendEmptyMessage(0);
                return;
            }
            Map<Double, ScoreData> pttMap = new TreeMap<>();
            Map<String, SongData> songDataMap = songRepo.getAllSongs();
            try {
                scoreRepo.getAllScores();
            } catch (Exception e) {
                scoreRepo = null;
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(activity);
                normalDialog.setTitle("存档读取失败");
                normalDialog.setMessage("请参照arcsong.db设置文件权限、所有者和用户组\n如果文件大小为12kb的话则为复制失败\n需要手动将st3文件放入data/data/xyz.hellocraft.arctools/databases/文件夹\n\n操作完成后请点击重试");
                normalDialog.setPositiveButton("我已知晓",
                        (dialog, which) -> {
                            dialog.dismiss();
//                            System.exit(100001);
                        });
                normalDialog.setCancelable(false);
                normalDialog.show();
                return;
            }
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