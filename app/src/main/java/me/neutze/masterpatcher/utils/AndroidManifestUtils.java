package me.neutze.masterpatcher.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import me.neutze.masterpatcher.R;

/**
 * Created by H1GHWAvE on 29/09/15.
 */
public class AndroidManifestUtils {

    public static List<String> getManifestPermissions(Context context, String name, String path) {
        String modifiedPath = context.getFilesDir().getParent() + "/" + name + "/";

        backupManifest(context, path, modifiedPath);

        List<String> permissions = getPermissions(context, modifiedPath);

        return permissions;
    }

    private static boolean backupManifest(Context context, String path, String modifiedPath) {
        if (!RunUtils.runBusyboxSUcmd("chmod 644 " + path)
                || !ZipUtils.backupManifest(context, path, modifiedPath)) {
            return false;
        }

        return true;
    }

    private static List<String> getPermissions(Context context, String modifiedPath) {
        String manifest = context.getResources().getString(R.string.manifest);
        List<String> permissions = new ArrayList<>();


        File file = new File(modifiedPath + manifest);

        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if (lineNum < 5)
                    Log.e(modifiedPath, line);
                if (line.contains("<uses-permisson")) {
                    Log.e(modifiedPath, line);
                }
            }
        } catch (FileNotFoundException e) {
            //handle this
        }

        return permissions;
    }
}
