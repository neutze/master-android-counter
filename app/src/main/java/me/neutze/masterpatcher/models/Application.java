package me.neutze.masterpatcher.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.neutze.masterpatcher.R;

/**
 * Created by H1GHWAvE on 24/09/15.
 */
public class Application {
    private int appId;
    private String name;
    private boolean license;
    private int logo;


    Application(int appId, String name, boolean license, int logo) {
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

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public static List<Application> getApplications(Context context) {
        List<Application> applicationsList = new ArrayList<>();

        applicationsList.add(new Application(1, "de.first.app", true, R.mipmap.ic_launcher));
        applicationsList.add(new Application(2, "de.second.app", true, R.mipmap.ic_launcher));
        applicationsList.add(new Application(3, "de.third.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(4, "de.four.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(5, "de.five.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(6, "de.six.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(7, "de.seven.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(8, "de.eight.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(9, "de.nine.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(10, "de.ten.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(11, "de.eleven.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(12, "de.twelve.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(13, "de.thirteen.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(14, "de.fourteen.app", false, R.mipmap.ic_launcher));
        applicationsList.add(new Application(15, "de.fifteen.app", false, R.mipmap.ic_launcher));

        return applicationsList;
    }
}
