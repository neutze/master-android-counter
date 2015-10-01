package me.neutze.masterpatcher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import me.neutze.masterpatcher.models.APKItem;

/**
 * Created by H1GHWAvE on 01/10/15.
 */
public class SharedPrefUtils {

    public static void saveAPKItem(Context context, APKItem apkItem) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(apkItem.getPkgName(), new Gson().toJson(apkItem, APKItem.class)).apply();
    }

    public static APKItem getAPKItem(Context context, String pkgName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String data = prefs.getString(pkgName, "DEFAULT");

        return new Gson().fromJson(data, APKItem.class);
    }
}
