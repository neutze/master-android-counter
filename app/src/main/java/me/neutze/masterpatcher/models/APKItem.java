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
import me.neutze.masterpatcher.utils.AdUtils;
import me.neutze.masterpatcher.utils.ModifiedUtils;
import me.neutze.masterpatcher.utils.OdexUtils;
import me.neutze.masterpatcher.utils.SDCardUtils;
import me.neutze.masterpatcher.utils.TimeUtils;

/**
 * Created by H1GHWAvE on 24/09/15.
 */
public class APKItem implements Parcelable {

    public boolean ads;
    public boolean billing;
    public boolean boot_ads;
    public boolean boot_custom;
    public boolean boot_lvl;
    public boolean boot_manual;
    public boolean custom;
    public boolean enable;
    public boolean hidden;
    public Drawable icon;
    public boolean lvl;
    public boolean modified;
    public String name;
    public boolean odex;
    public boolean on_sd;
    public String pkgName;
    public boolean selected;
    public String statusi;
    public int stored;
    public int storepref;
    public boolean system;
    public int updatetime;

    public APKItem(Context context, String application) {
        this.pkgName = application;

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

        if (OdexUtils.isOdex(applicationPath)) {
            this.odex = true;
        } else {
            this.odex = false;
        }

        this.updatetime = (int) (TimeUtils.getfirstInstallTime(packageInfo, pkgName) / 1000);

        this.modified = ModifiedUtils.isModified(pkgName);

        if (packageInfo.applicationInfo.flags == 1) {
            this.system = true;
        }

        if (packageInfo.activities != null) {
            for (int i = 0; i < packageInfo.activities.length; i++) {
                if (AdUtils.isAds(packageInfo.activities[i].name)) {
                    this.ads = true;
                }
            }
        }
    }

    protected APKItem(Parcel in) {
        ads = in.readByte() != 0;
        billing = in.readByte() != 0;
        boot_ads = in.readByte() != 0;
        boot_custom = in.readByte() != 0;
        boot_lvl = in.readByte() != 0;
        boot_manual = in.readByte() != 0;
        custom = in.readByte() != 0;
        enable = in.readByte() != 0;
        hidden = in.readByte() != 0;
        lvl = in.readByte() != 0;
        modified = in.readByte() != 0;
        name = in.readString();
        odex = in.readByte() != 0;
        on_sd = in.readByte() != 0;
        pkgName = in.readString();
        selected = in.readByte() != 0;
        statusi = in.readString();
        stored = in.readInt();
        storepref = in.readInt();
        system = in.readByte() != 0;
        updatetime = in.readInt();
    }

    public static final Creator<APKItem> CREATOR = new Creator<APKItem>() {
        @Override
        public APKItem createFromParcel(Parcel in) {
            return new APKItem(in);
        }

        @Override
        public APKItem[] newArray(int size) {
            return new APKItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ads ? 1 : 0));
        dest.writeByte((byte) (billing ? 1 : 0));
        dest.writeByte((byte) (boot_ads ? 1 : 0));
        dest.writeByte((byte) (boot_custom ? 1 : 0));
        dest.writeByte((byte) (boot_lvl ? 1 : 0));
        dest.writeByte((byte) (boot_manual ? 1 : 0));
        dest.writeByte((byte) (custom ? 1 : 0));
        dest.writeByte((byte) (enable ? 1 : 0));
        dest.writeByte((byte) (hidden ? 1 : 0));
        dest.writeByte((byte) (lvl ? 1 : 0));
        dest.writeByte((byte) (modified ? 1 : 0));
        dest.writeString(name);
        dest.writeByte((byte) (odex ? 1 : 0));
        dest.writeByte((byte) (on_sd ? 1 : 0));
        dest.writeString(pkgName);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(statusi);
        dest.writeInt(stored);
        dest.writeInt(storepref);
        dest.writeByte((byte) (system ? 1 : 0));
        dest.writeInt(updatetime);
    }

    public static List<APKItem> getApplications(Context context) {
        List<APKItem> applicationsList = new ArrayList<>();

        List<String> installedApplications = Shell.SU.run("ls " + context.getResources().getString(R.string.app_folder));
        for (String application : installedApplications) {

            applicationsList.add(new APKItem(context, application));
        }

        return applicationsList;
    }
}