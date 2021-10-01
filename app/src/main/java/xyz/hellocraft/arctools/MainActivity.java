package xyz.hellocraft.arctools;

import static xyz.hellocraft.arctools.utils.Tools.openApp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hjq.toast.ToastUtils;

import java.util.List;

import xyz.hellocraft.arctools.databinding.ActivityMainBinding;
import xyz.hellocraft.arctools.utils.RootKit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToastUtils.init(getApplication());
        ToastUtils.setGravity(Gravity.BOTTOM, 0, 50);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnB30.setOnClickListener(view -> startActivity(new Intent(this, LocalScoreActivity.class)));
        binding.btnFrag.setOnClickListener(view -> startActivity(new Intent(this, FragActivity.class)));
        boolean hasArc = false;
        PackageManager packageManager = getPackageManager();
        //获取所有安装的app
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo info : installedPackages) {
//            Log.d("PM", "getPackageInfo: "+info.packageName);
            if (info.packageName.equals("moe.low.arc")) {
                hasArc = true;
                break;
            }
        }
        if (!hasArc) {
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("未检测到Arcaea");
            normalDialog.setMessage("请检查安装后重试");
            normalDialog.setPositiveButton("我已知晓",
                    (dialog, which) -> {
                        dialog.dismiss();
//                            System.exit(100001);
                    });
            normalDialog.setCancelable(false);
            normalDialog.show();
        } else {
//            Toast.makeText(this,"Arcaea Found!",Toast.LENGTH_SHORT).show();
            binding.arcCheckBox.setChecked(true);
        }

        ToastUtils.show("本app依赖root权限执行绝大多数操作\n请给予Root权限");
        boolean haveRoot = RootKit.haveRoot();
        if (!haveRoot) {
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("设备没有Root");
            normalDialog.setMessage("本app依赖root权限执行绝大多数操作！\n请给予Root权限！");
            normalDialog.setPositiveButton("我已知晓",
                    (dialog, which) -> {
                        dialog.dismiss();
                    });
            for (PackageInfo info : installedPackages) {
                if (info.packageName.equals("com.topjohnwu.magisk")) {
                    normalDialog.setMessage("本app依赖root权限执行绝大多数操作！\n请给予Root权限！\n\n检测到面具已安装\n是否打开面具给予权限？");
                    normalDialog.setPositiveButton("打开面具",
                            (dialog, which) -> {
                                openApp("com.topjohnwu.magisk", this);
                                dialog.dismiss();
                            });
                    normalDialog.setNegativeButton("取消",
                            (dialog, which) -> {
                                dialog.dismiss();
                            });
                }
            }


            normalDialog.setCancelable(false);
            normalDialog.show();
        } else {
            binding.rootCheckBox.setChecked(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
            normalDialog.setTitle("检测到Android 11或更高版本");
            normalDialog.setMessage("高版本Android因为未知原因su相关指令可能会执行失败");
            normalDialog.setPositiveButton("我已知晓",
                    (dialog, which) -> {
                        dialog.dismiss();
                    });
            normalDialog.setCancelable(false);
            normalDialog.show();
        }
    }

}