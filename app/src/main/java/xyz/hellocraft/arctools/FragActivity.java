package xyz.hellocraft.arctools;

import static java.lang.Integer.parseInt;
import static xyz.hellocraft.arctools.utils.Tools.openApp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.toast.ToastUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import xyz.hellocraft.arctools.databinding.ActivityFragBinding;
import xyz.hellocraft.arctools.utils.RootKit;

public class FragActivity extends AppCompatActivity {

    private ActivityFragBinding binding;

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int fr_v = parseInt(binding.editTextFr.getText().toString());
            if (fr_v > 29997 || fr_v < 0) {
                fr_v = 29997;
                binding.editTextFr.setText(fr_v+"");
            }
            Log.d("fr_v",fr_v+"");
            String fr_k = encrypt2MD5(fr_v + "ok" + fr_v);
            Log.d("fr_k", fr_k);
//            Log.d("data dir",getDataDir().toString());
            SharedPreferences local_arc_pref = getSharedPreferences("Cocos2dxPrefsFile",0);
            local_arc_pref.edit()
                    .putInt("fr_v",fr_v)
                    .putString("fr_k",fr_k)
                    .apply();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFragBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        refresh();
        binding.editTextFr.addTextChangedListener(textWatcher);
        binding.buttonApply.setOnClickListener(view -> {
            RootKit.execRootCmdSilent("cp -f /data/user/0/xyz.hellocraft.arctools/shared_prefs/Cocos2dxPrefsFile.xml /data/user/0/moe.low.arc/shared_prefs/");
            ToastUtils.show("修改已应用到Arcae");
            refresh();
        });
        binding.buttonReset.setOnClickListener(view -> refresh());
        binding.buttonStart.setOnClickListener(view -> {
            try {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName("moe.low.arc", "low.moe.AppActivity");
                intent.setComponent(cn);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                PackageManager packageManager = getPackageManager();
                //获取所有安装的app
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
                for (PackageInfo info : installedPackages) {
//            Log.d("PM", "getPackageInfo: "+info.packageName);
                    if (info.packageName.equals("com.catchingnow.icebox")) {
                        androidx.appcompat.app.AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
                        normalDialog.setTitle("Arcaea 启动失败");
                        normalDialog.setMessage("检测到 Icebox 已安装\n请检查是否忘记解冻Arcaea");
                        normalDialog.setPositiveButton("我已知晓",
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    openApp("com.catchingnow.icebox",this);
//                            System.exit(100001);
                                });
                        normalDialog.setNegativeButton("取消", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        });
                        normalDialog.setCancelable(false);
                        normalDialog.show();
                    }
                }
            }
        });
    }
    private void refresh() {
        SharedPreferences local_arc_pref = getSharedPreferences("Cocos2dxPrefsFile",0);
        local_arc_pref.edit().putInt("create",0).apply(); // create empty file
        RootKit.execRootCmdSilent("cp -f /data/user/0/moe.low.arc/shared_prefs/Cocos2dxPrefsFile.xml /data/user/0/xyz.hellocraft.arctools/shared_prefs/");
        RootKit.execRootCmdSilent("am force-stop moe.low.arc");
        binding.editTextFr.setText(local_arc_pref.getInt("fr_v",0)+"");
    }

    private String encrypt2MD5(String rawString) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(rawString.getBytes(StandardCharsets.UTF_8));
            byte[] hash = md5.digest();
            for (byte b : hash) {
                if ((0xff & b) < 0x10) {
                    hexString.append("0").append(Integer.toHexString((0xFF & b)));
                } else {
                    hexString.append(Integer.toHexString(0xFF & b));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
}