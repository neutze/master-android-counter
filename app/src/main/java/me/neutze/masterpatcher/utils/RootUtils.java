package me.neutze.masterpatcher.utils;

import eu.chainfire.libsuperuser.Shell;

/**
 * Sources:
 * http://su.chainfire.eu/#how
 */

public class RootUtils {

    public static boolean isSUavailable() {
        return Shell.SU.available();
    }
}
