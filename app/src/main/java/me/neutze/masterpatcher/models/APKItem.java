package me.neutze.masterpatcher.models;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import net.erdfelt.android.apk.AndroidApk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.utils.AdUtils;
import me.neutze.masterpatcher.utils.ModifiedUtils;
import me.neutze.masterpatcher.utils.OdexUtils;
import me.neutze.masterpatcher.utils.SDCardUtils;
import me.neutze.masterpatcher.utils.SharedPrefUtils;
import me.neutze.masterpatcher.utils.TimeUtils;

/**
 * Created by H1GHWAvE on 24/09/15.
 */
public class APKItem implements Parcelable {

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
    private boolean ads;
    private boolean billing;
    private boolean custom;
    private boolean enable;
    private Drawable icon;
    private boolean lvl;
    private boolean modified;
    private String name;
    private boolean odex;
    private boolean on_sd;
    private String pkgName;
    private int stored;
    private boolean system;
    private int updatetime;
    private List<String> permissions;
    private List<String> advertisments;

    public APKItem(Context context, String application) {
        this.pkgName = application;

        Log.e(pkgName, "newAPK");

        String applicationPath = context.getResources().getString(R.string.app_folder) + application + "/" + context.getResources().getString(R.string.base_apk);

        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(applicationPath, 1);
        packageInfo.applicationInfo.sourceDir = applicationPath;
        packageInfo.applicationInfo.publicSourceDir = applicationPath;

        this.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        this.icon = packageInfo.applicationInfo.loadIcon(packageManager);
        this.on_sd = SDCardUtils.isInstalledOnSdCard(context, applicationPath);
        this.enable = packageInfo.applicationInfo.enabled;

        this.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();

        this.odex = OdexUtils.isOdex(applicationPath);

        this.updatetime = (int) (TimeUtils.getfirstInstallTime(packageInfo, pkgName) / 1000);

        this.modified = ModifiedUtils.isModified(pkgName);

        if (packageInfo.applicationInfo.flags == 1) {
            this.system = true;
        }

        if (packageInfo.activities != null) {
            advertisments = new ArrayList<>();

            for (int i = 0; i < packageInfo.activities.length; i++) {
                if (AdUtils.isAds(packageInfo.activities[i].name)) {
                    advertisments.add(packageInfo.activities[i].name);
                    this.ads = true;
                }
            }
        }

        try {
            AndroidApk apk = new AndroidApk(new File(applicationPath));
            permissions = apk.getPermissions();
            if (permissions != null) {
                for (String permission : apk.getPermissions()) {
                    if (permission.equals("com.android.vending.CHECK_LICENSE")) {
                        lvl = true;
                    }
                    if (permission.equals("com.android.vending.BILLING")) {
                        billing = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        stored = 0;
        if (ads) {
            stored += 100;
        }
        if (billing) {
            stored += 10;
        }
        if (lvl) {
            stored += 1;
        }
    }


    protected APKItem(Parcel in) {
        ads = in.readByte() != 0x00;
        billing = in.readByte() != 0x00;
        custom = in.readByte() != 0x00;
        enable = in.readByte() != 0x00;
        icon = (Drawable) in.readValue(Drawable.class.getClassLoader());
        lvl = in.readByte() != 0x00;
        modified = in.readByte() != 0x00;
        name = in.readString();
        odex = in.readByte() != 0x00;
        on_sd = in.readByte() != 0x00;
        pkgName = in.readString();
        stored = in.readInt();
        system = in.readByte() != 0x00;
        updatetime = in.readInt();
        if (in.readByte() == 0x01) {
            permissions = new ArrayList<>();
            in.readList(permissions, String.class.getClassLoader());
        } else {
            permissions = null;
        }
        if (in.readByte() == 0x01) {
            advertisments = new ArrayList<>();
            in.readList(advertisments, String.class.getClassLoader());
        } else {
            advertisments = null;
        }
    }

    public static List<APKItem> getApplications(Context context) {
        List<APKItem> applicationsList = new ArrayList<>();

        List<String> installedApplications = Shell.SU.run("ls " + context.getResources().getString(R.string.app_folder));
        for (String application : installedApplications) {
            APKItem apkItem = SharedPrefUtils.getAPKItem(context, application);

            if (apkItem == null) {
                apkItem = new APKItem(context, application);
            } else {
                SharedPrefUtils.saveAPKItem(context, apkItem);
            }

            applicationsList.add(apkItem);
        }

        return applicationsList;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean getLvl() {
        return lvl;
    }

    public boolean getBilling() {
        return billing;
    }

    public boolean getAds() {
        return ads;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ads ? 0x01 : 0x00));
        dest.writeByte((byte) (billing ? 0x01 : 0x00));
        dest.writeByte((byte) (custom ? 0x01 : 0x00));
        dest.writeByte((byte) (enable ? 0x01 : 0x00));
        dest.writeValue(icon);
        dest.writeByte((byte) (lvl ? 0x01 : 0x00));
        dest.writeByte((byte) (modified ? 0x01 : 0x00));
        dest.writeString(name);
        dest.writeByte((byte) (odex ? 0x01 : 0x00));
        dest.writeByte((byte) (on_sd ? 0x01 : 0x00));
        dest.writeString(pkgName);
        dest.writeInt(stored);
        dest.writeByte((byte) (system ? 0x01 : 0x00));
        dest.writeInt(updatetime);
        if (permissions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(permissions);
        }
        if (advertisments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(advertisments);
        }
    }

    public String getPkgName() {
        return pkgName;
    }
}