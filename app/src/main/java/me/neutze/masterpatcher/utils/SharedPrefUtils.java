package me.neutze.masterpatcher.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.models.APKItem;

/**
 * Created by H1GHWAvE on 01/10/15.
 */
public class SharedPrefUtils {

    public static void saveAPKItem(Context context, APKItem apkItem) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        sharedPref.edit().putString(apkItem.getPkgName(), new Gson().toJson(apkItem, APKItem.class)).apply();
    }

    public static APKItem getAPKItem(Context context, String pkgName) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);

        return new Gson().fromJson(sharedPref.getString(pkgName, ""), APKItem.class);
    }

    public static void saveVersion(Context context, int version) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        sharedPref.edit().putInt(context.getResources().getString(R.string.version_code), version).apply();
    }

    public static int getVersion(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);

        return sharedPref.getInt(context.getResources().getString(R.string.version_code), 0);
    }

    public static void clearAllPrefs(Context context) {
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit().clear().apply();
    }
}
