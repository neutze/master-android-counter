package me.neutze.masterpatcher.models;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import me.neutze.masterpatcher.R;

/**
 * Created by H1GHWAvE on 24/09/15.
 */
public class APKItem implements Parcelable {

    private String backupfile;
    private Drawable icon;
    private String name = "Bad file";
    private String packageName;
    private int versionCode;
    private String versionName;


    public APKItem(String backupfile, Drawable icon, String name, String packageName, int versionCode, String versionName) {
        this.backupfile = backupfile;
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionName = versionName;
    }

    public String getBackupfile() {
        return backupfile;
    }

    public void setBackupfile(String backupfile) {
        this.backupfile = backupfile;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public static List<APKItem> getApplications(Context context) {


        List<APKItem> applicationsList = new ArrayList<>();

        List<String> installedApplications = Shell.SU.run("ls " + context.getResources().getString(R.string.app_folder));
        for (String application : installedApplications) {

            String APKFilePath = context.getResources().getString(R.string.app_folder) + application + "/" + context.getResources().getString(R.string.base_apk);

            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(APKFilePath, 0);

            packageInfo.applicationInfo.sourceDir = APKFilePath;
            packageInfo.applicationInfo.publicSourceDir = APKFilePath;

            String backupfileItem = null;
            Drawable iconItem = packageInfo.applicationInfo.loadIcon(packageManager);
            String nameItem = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            String packageNameItem = packageInfo.applicationInfo.packageName;
            int versionCodeItem = packageInfo.versionCode;
            String versionNameItem = packageInfo.versionName;

            applicationsList.add(new APKItem(backupfileItem, iconItem, nameItem, packageNameItem, versionCodeItem, versionNameItem));
        }

        return applicationsList;
    }

    protected APKItem(Parcel in) {
        backupfile = in.readString();
        icon = (Drawable) in.readValue(Drawable.class.getClassLoader());
        name = in.readString();
        packageName = in.readString();
        versionCode = in.readInt();
        versionName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backupfile);
        dest.writeValue(icon);
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeInt(versionCode);
        dest.writeString(versionName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<APKItem> CREATOR = new Parcelable.Creator<APKItem>() {
        @Override
        public APKItem createFromParcel(Parcel in) {
            return new APKItem(in);
        }

        @Override
        public APKItem[] newArray(int size) {
            return new APKItem[size];
        }
    };
}