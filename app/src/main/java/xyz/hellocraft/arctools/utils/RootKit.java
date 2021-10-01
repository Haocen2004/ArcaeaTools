package xyz.hellocraft.arctools.utils;


import android.util.Log;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RootKit {

    private static final String TAG = "RootKit";
    private static boolean mHaveRoot = false;
    private static boolean mHaveBusyBox = false;

    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo tryGetRoot");
            if (ret != -1) {
                Log.i(TAG, "Device root get.");
                mHaveRoot = true;
                if (execRootCmdSilent("busybox echo tryGetRoot") != -1) {
                    mHaveBusyBox = true;
                    Log.i(TAG, "Device has BusyBox");
                }
            } else {
                Log.i(TAG, "Device not root.");
            }
        } else {
            Log.i(TAG, "Already get root.");
        }
        return mHaveRoot;
    }

    public static int execRootCmdSilent(String cmd) {
        try {
            Process process = new ProcessBuilder("su").redirectErrorStream(true).start();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter((process.getOutputStream()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (mHaveBusyBox) {
                Log.d(TAG, "execRootCmd: busybox "+cmd);
                outputStreamWriter.write("busybox "+cmd + "\n");
            } else {
                Log.d(TAG, "execRootCmd: "+cmd);
                outputStreamWriter.write(cmd + "\n");

            }            outputStreamWriter.write("exit\n");
            outputStreamWriter.flush();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("Permission denied") || line.contains("inaccessible or not found")) {
                    return -1;
                }
                if (line.contains("applet not found")) {
                    mHaveBusyBox = !mHaveBusyBox;
                    execRootCmdSilent(cmd);
                    mHaveBusyBox = !mHaveBusyBox;
                }
                Log.d(TAG, "execReturn: "+line);
//                ToastUtils.show(line);
            }
            outputStreamWriter.close();
            bufferedReader.close();
            process.destroy();
            return 0;
        } catch (Exception e) {
//            e.printStackTrace();
            Log.e(TAG,"Command Execute Failed",e);
            return -1;
        }
    }
}