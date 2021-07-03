package xyz.hellocraft.arctools.utils;


import android.util.Log;

        import java.io.DataInputStream;
        import java.io.DataOutputStream;
        import java.io.IOException;

public class RootKit {

    private static final String TAG = "RootKit";
    private static boolean mHaveRoot = false;

    public static boolean haveRoot() {
        if (!mHaveRoot) {
            int ret = execRootCmdSilent("echo tryGetRoot");
            if (ret != -1) {
                Log.i(TAG, "Device root get.");
                mHaveRoot = true;
            } else {
                Log.i(TAG, "Device not root.");
            }
        } else {
            Log.i(TAG, "Already get root.");
        }
        return mHaveRoot;
    }

    public static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}