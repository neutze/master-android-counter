package me.neutze.masterpatcher.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by H1GHWAvE on 29/09/15.
 */
public class RunUtils {


    public static boolean runBusyboxSUcmd(String command) {
        String busybox = getBusybox();

        if (busybox != null) {
            command = busybox + " " + command;
            return runSUcmd(command);
        }

        return false;
    }

    private static String getBusybox() {
        if (new File("/system/bin/failsafe/busybox").exists()) {
            return "/system/bin/failsafe/busybox";
        } else if (new File("/system/xbin/busybox").exists()) {
            return "/system/xbin/busybox";
        } else if (new File("/system/bin/busybox").exists()) {
            return "/system/bin/busybox";
        }
        return null;
    }

    private static boolean runSUcmd(String command) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());

            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
