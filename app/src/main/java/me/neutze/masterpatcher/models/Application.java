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

/**
 * Created by H1GHWAvE on 24/09/15.
 */
public class Application implements Parcelable {
    private int appId;
    private String name;
    private boolean license;
    private Drawable logo;


    Application(int appId, String name, boolean license, Drawable logo) {
        this.appId = appId;
        this.name = name;
        this.license = license;
        this.logo = logo;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasLicense() {
        return license;
    }

    public void setLicense(boolean license) {
        this.license = license;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }

    public static List<Application> getApplications(Context context) {
        List<Application> applicationsList = new ArrayList<>();

        List<String> installedApplications = Shell.SU.run("ls /data/app");
        for (String application : installedApplications) {

            String APKFilePath = "data/app/" + application + "/base.apk";

            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(APKFilePath, 0);

            pi.applicationInfo.sourceDir = APKFilePath;
            pi.applicationInfo.publicSourceDir = APKFilePath;
            //

            Drawable APKicon = pi.applicationInfo.loadIcon(pm);
            String AppName = (String) pi.applicationInfo.loadLabel(pm);
            //pi.applicationInfo.load

            applicationsList.add(new Application(1, AppName, true, APKicon));
        }

        return applicationsList;
    }

    protected Application(Parcel in) {
        appId = in.readInt();
        name = in.readString();
        license = in.readByte() != 0x00;
        logo = (Drawable) in.readValue(Drawable.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(appId);
        dest.writeString(name);
        dest.writeByte((byte) (license ? 0x01 : 0x00));
        dest.writeValue(logo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Application> CREATOR = new Parcelable.Creator<Application>() {
        @Override
        public Application createFromParcel(Parcel in) {
            return new Application(in);
        }

        @Override
        public Application[] newArray(int size) {
            return new Application[size];
        }
    };
}
