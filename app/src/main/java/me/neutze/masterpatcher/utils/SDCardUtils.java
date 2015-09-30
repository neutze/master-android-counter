package me.neutze.masterpatcher.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

/**
 * Created by H1GHWAvE on 30/09/15.
 */
public class SDCardUtils {
    public static boolean isInstalledOnSdCard(Context context, String application) {
        // check for API level 8 and higher
        PackageManager packageManager = context.getPackageManager();

        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(application, 0);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                return (applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
            } catch (PackageManager.NameNotFoundException e) {
                // ignore
            }
        }

        // check for API level 7 - check files dir
        try {
            String filesDir = packageManager.getPackageInfo(application, 0).applicationInfo.sourceDir;
            if (filesDir.startsWith("/data/")) {
                return false;
            } else if (filesDir.contains("/mnt/") || filesDir.contains(Environment.getExternalStorageDirectory().getPath())) {

                return true;
            }
        } catch (Throwable e) {
            // ignore
        }

        return false;
    }
}
