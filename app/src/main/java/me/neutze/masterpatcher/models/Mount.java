package me.neutze.masterpatcher.models;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by H1GHWAvE on 29/09/15.
 */
public class Mount {
    protected final File mDevice;
    protected final Set<String> mFlags;
    protected final File mMountPoint;
    protected final String mType;

    public Mount(File file, File file2, String str, String str2) {
        this.mDevice = file;
        this.mMountPoint = file2;
        this.mType = str;
        this.mFlags = new HashSet(Arrays.asList(str2.split(",")));
    }

    public File getDevice() {
        return this.mDevice;
    }

    public Set<String> getFlags() {
        return this.mFlags;
    }

    public File getMountPoint() {
        return this.mMountPoint;
    }

    public String getType() {
        return this.mType;
    }

    public String toString() {
        return String.format("%s on %s type %s %s", this.mDevice, this.mMountPoint, this.mType, this.mFlags);
    }
}