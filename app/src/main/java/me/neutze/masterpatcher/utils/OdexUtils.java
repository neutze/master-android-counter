package me.neutze.masterpatcher.utils;

import java.io.File;

/**
 * Created by H1GHWAvE on 30/09/15.
 */
public class OdexUtils {
    public static boolean isOdex(String str) {
        try {
            if (new File(changeExtension(str, "odex")).exists()) {
                return true;
            }
            File file = new File(str);
            if (new File(getDirs(file) + "/arm/" + changeExtension(file.getName(), "odex")).exists()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String changeExtension(String str, String str2) {
        if (str == null) {
            return "";
        }
        String[] split = str.split("\\.");
        String newString = "";
        int i = 0;
        while (i < split.length) {
            newString = i < split.length + -1 ? newString + split[i] + "." : newString + str2;
            i++;
        }
        return newString;
    }

    public static File getDirs(File file) {
        String[] split = file.toString().split(File.separator);
        String str = "";
        for (int i = 0; i < split.length; i++) {
            if (i != split.length - 1) {
                str = str + split[i] + File.separator;
            }
        }
        return new File(str);
    }
}
