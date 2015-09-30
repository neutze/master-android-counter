package me.neutze.masterpatcher.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;

import me.neutze.masterpatcher.R;

/**
 * Created by H1GHWAvE on 29/09/15.
 */
public class ZipUtils {
    /**
     * public static void unzip(File file) {
     * try {
     * InputStream fileInputStream = new FileInputStream(file);
     * java.util.zip.ZipInputStream zipInputStream = new java.util.zip.ZipInputStream(fileInputStream);
     * for (ZipEntry nextEntry = zipInputStream.getNextEntry(); nextEntry != null; nextEntry = zipInputStream.getNextEntry()) {
     * String name = nextEntry.getName();
     * if (name.equals("classes.dex")) {
     * FileOutputStream fileOutputStream = new FileOutputStream(getInstance().getFilesDir().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + "classes.dex");
     * byte[] bArr = new byte[InternalZipConstants.UFT8_NAMES_FLAG];
     * while (true) {
     * int read = zipInputStream.read(bArr);
     * if (read == -1) {
     * break;
     * }
     * fileOutputStream.write(bArr, SETTINGS_VIEWSIZE_SMALL, read);
     * }
     * zipInputStream.closeEntry();
     * fileOutputStream.close();
     * v = SETTINGS_SORTBY_NAME;
     * }
     * if (name.equals("AndroidManifest.xml")) {
     * FileOutputStream fileOutputStream2 = new FileOutputStream(getInstance().getFilesDir().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + "AndroidManifest.xml");
     * byte[] bArr2 = new byte[InternalZipConstants.UFT8_NAMES_FLAG];
     * while (true) {
     * int read2 = zipInputStream.read(bArr2);
     * if (read2 == -1) {
     * break;
     * }
     * fileOutputStream2.write(bArr2, SETTINGS_VIEWSIZE_SMALL, read2);
     * }
     * zipInputStream.closeEntry();
     * fileOutputStream2.close();
     * v2 = SETTINGS_SORTBY_NAME;
     * }
     * if (v != null && r0 != null) {
     * break;
     * }
     * }
     * zipInputStream.close();
     * fileInputStream.close();
     * } catch (Exception e) {
     * try {
     * ZipFile zipFile = new ZipFile(file);
     * zipFile.extractFile("classes.dex", getInstance().getFilesDir().getAbsolutePath());
     * zipFile.extractFile("AndroidManifest.xml", getInstance().getFilesDir().getAbsolutePath());
     * } catch (ZipException e2) {
     * System.out.println("Error classes.dex decompress! " + e2);
     * System.out.println("Exception e1" + e.toString());
     * } catch (Exception e3) {
     * System.out.println("Error classes.dex decompress! " + e3);
     * System.out.println("Exception e1" + e.toString());
     * }
     * System.out.println("Exception e" + e.toString());
     * }
     * }
     **/

    public static boolean backupManifest(Context context, String path, String modifiedPath) {
        String manifest = context.getResources().getString(R.string.manifest);

        try {

            File modifiedDirectory = new File(modifiedPath);
            if (!modifiedDirectory.exists()) {
                if (!modifiedDirectory.mkdirs()) {
                    Log.e("JOHANNES", "Cannot create directory.");
                }
            }

            File modifiedManifest = new File(modifiedPath + manifest);
            if (!modifiedManifest.exists()) {
                if (!modifiedManifest.createNewFile()) {
                    Log.e("JOHANNES", "Cannot create file.");
                }
                return unzipManifest(path, manifest, modifiedManifest);
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean unzipManifest(String path, String manifest, File modifiedManifest) {
        try {
            InputStream fileInputStream = new FileInputStream(path);
            java.util.zip.ZipInputStream zipInputStream = new java.util.zip.ZipInputStream(fileInputStream);

            while (true) {
                ZipEntry nextEntry = zipInputStream.getNextEntry();

                if (nextEntry == null) {
                    break;
                } else if (nextEntry.getName().equals(manifest)) {

                    FileOutputStream fileOutputStream = new FileOutputStream(modifiedManifest);
                    byte[] byteArray = new byte[8192];

                    while (true) {
                        int read = zipInputStream.read(byteArray);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(byteArray, 0, read);
                    }

                    zipInputStream.closeEntry();
                    fileOutputStream.close();
                }
            }

            zipInputStream.close();
            fileInputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
