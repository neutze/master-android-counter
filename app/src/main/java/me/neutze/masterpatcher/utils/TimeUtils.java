package me.neutze.masterpatcher.utils;

import android.content.pm.PackageInfo;
import android.os.Build;

import java.io.File;

/**
 * Created by H1GHWAvE on 30/09/15.
 */
public class TimeUtils {
    public static long getfirstInstallTime(PackageInfo packageInfo, String str) {
        if (Build.VERSION.SDK_INT <= 8) {
            return new File(packageInfo.applicationInfo.sourceDir).lastModified();
        }

        return packageInfo.lastUpdateTime;
    }
}
