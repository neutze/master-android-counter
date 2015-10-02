package me.neutze.masterpatcher.models;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import net.erdfelt.android.apk.AndroidApk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;
import me.neutze.masterpatcher.R;
import me.neutze.masterpatcher.utils.AdUtils;
import me.neutze.masterpatcher.utils.ModifiedUtils;
import me.neutze.masterpatcher.utils.OdexUtils;
import me.neutze.masterpatcher.utils.SDCardUtils;
import me.neutze.masterpatcher.utils.SharedPrefUtils;
import me.neutze.masterpatcher.utils.TimeUtils;

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
    private transient Drawable icon;
    private boolean lvl;
    private boolean modified;
    private String name;
    private boolean odex;
    private boolean on_sd;
    private String pkgName;
    private int attributes;
    private boolean system;
    private int updatetime;
    private String applicationPath;
    private List<String> permissions;
    private List<String> advertisments;
    private boolean edited;


    private APKItem(Context context, String pkgName, PackageManager packageManager) {
        this.pkgName = pkgName;

        this.applicationPath = context.getResources().getString(R.string.app_folder) + pkgName + "/" + context.getResources().getString(R.string.base_apk);
        PackageInfo packageInfo = setPackageInfo(packageManager, applicationPath);

        this.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        this.icon = packageInfo.applicationInfo.loadIcon(packageManager);
        this.on_sd = SDCardUtils.isInstalledOnSdCard(context, applicationPath);
        this.enable = packageInfo.applicationInfo.enabled;

        this.name = packageInfo.applicationInfo.loadLabel(packageManager).toString();

        this.odex = OdexUtils.isOdex(applicationPath);

        this.updatetime = (int) (TimeUtils.getfirstInstallTime(packageInfo, this.pkgName) / 1000);

        this.modified = ModifiedUtils.isModified(this.pkgName);

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

        attributes = 0;
        if (ads) {
            attributes += 100;
        }
        if (billing) {
            attributes += 10;
        }
        if (lvl) {
            attributes += 1;
        }

        this.edited = false;
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
        attributes = in.readInt();
        system = in.readByte() != 0x00;
        updatetime = in.readInt();
        applicationPath = in.readString();
        if (in.readByte() == 0x01) {
            permissions = new ArrayList<String>();
            in.readList(permissions, String.class.getClassLoader());
        } else {
            permissions = null;
        }
        if (in.readByte() == 0x01) {
            advertisments = new ArrayList<String>();
            in.readList(advertisments, String.class.getClassLoader());
        } else {
            advertisments = null;
        }
        edited = in.readByte() != 0x00;
    }

    public static List<APKItem> getApplications(Context context) {
        List<APKItem> applicationsList = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;

        List<String> installedApplications = Shell.SU.run("ls " + context.getResources().getString(R.string.app_folder));
        for (String application : installedApplications) {
            APKItem apkItem = SharedPrefUtils.getAPKItem(context, application);

            if (apkItem == null) {
                apkItem = getApkItem(context, application, packageManager);
            } else {
                if (apkItem.isEdited()) {
                    apkItem = getApkItem(context, application, packageManager);
                } else {
                    packageInfo = setPackageInfo(packageManager, apkItem.getApplicationPath());
                    Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);

                    apkItem.setIcon(icon);
                }
            }
            if (apkItem.getAttributes() != 0 || apkItem.getPermissions() != null)
                applicationsList.add(apkItem);
        }

        Collections.sort(applicationsList, new Comparator<APKItem>() {
            @Override
            public int compare(APKItem o1, APKItem o2) {
                return o1.getName().compareTo(o2.getName());
            }

        });

        return applicationsList;
    }

    private static APKItem getApkItem(Context context, String application, PackageManager packageManager) {
        APKItem apkItem = new APKItem(context, application, packageManager);
        SharedPrefUtils.saveAPKItem(context, apkItem);

        return apkItem;
    }

    private static PackageInfo setPackageInfo(PackageManager packageManager, String applicationPath) {
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(applicationPath, 0);
        packageInfo.applicationInfo.sourceDir = applicationPath;
        packageInfo.applicationInfo.publicSourceDir = applicationPath;
        return packageInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
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

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public boolean isAds() {
        return ads;
    }

    public void setAds(boolean ads) {
        this.ads = ads;
    }

    public boolean isBilling() {
        return billing;
    }

    public void setBilling(boolean billing) {
        this.billing = billing;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isLvl() {
        return lvl;
    }

    public void setLvl(boolean lvl) {
        this.lvl = lvl;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isOdex() {
        return odex;
    }

    public void setOdex(boolean odex) {
        this.odex = odex;
    }

    public boolean isOn_sd() {
        return on_sd;
    }

    public void setOn_sd(boolean on_sd) {
        this.on_sd = on_sd;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public int getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }

    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
    }

    public List<String> getAdvertisments() {
        return advertisments;
    }

    public void setAdvertisments(List<String> advertisments) {
        this.advertisments = advertisments;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
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
        dest.writeInt(attributes);
        dest.writeByte((byte) (system ? 0x01 : 0x00));
        dest.writeInt(updatetime);
        dest.writeString(applicationPath);
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
        dest.writeByte((byte) (edited ? 0x01 : 0x00));
    }
}