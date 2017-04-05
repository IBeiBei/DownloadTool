package com.bfloral.ibeibei.downloadtool.util.cache;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by MingLi on 10/9/16.
 */

public class DiskCacheUtil {
    private static final String DISKCACHE_DIR_FILES_SD = "files_sd";
    private static final String DISKCACHE_DIR_FILES_CACHE = "files_cache";
    private static final int DISKCACHE_MAXSIZE = 500 * 1024 * 1024;//500M
    private static HashMap<String, SimpleDiskCache> cacheInstances = new HashMap<>();
    /**
     * get cache instance
     *
     * @param context
     * @return
     */
    public static SimpleDiskCache getDiskCacheInstance(Context context) {
        if (isHasStoragePermission(context)) {
            if (cacheInstances.containsKey(DISKCACHE_DIR_FILES_SD)) {
                return cacheInstances.get(DISKCACHE_DIR_FILES_SD);
            }
            try {
                cacheInstances.put(DISKCACHE_DIR_FILES_SD, SimpleDiskCache.open(getDiskCacheDir(context), getAppVersion(context), DISKCACHE_MAXSIZE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cacheInstances.get(DISKCACHE_DIR_FILES_SD);
        } else {
            if (cacheInstances.containsKey(DISKCACHE_DIR_FILES_CACHE)) {
                return cacheInstances.get(DISKCACHE_DIR_FILES_CACHE);
            }
            try {
                cacheInstances.put(DISKCACHE_DIR_FILES_CACHE, SimpleDiskCache.open(getDiskCacheDir(context), getAppVersion(context), DISKCACHE_MAXSIZE));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cacheInstances.get(DISKCACHE_DIR_FILES_CACHE);
        }
    }

    /**
     * remove cache by key
     *
     * @param context
     * @param key
     * @return
     * @throws IOException
     */
    public static boolean removeCacheByKey(Context context, String key) throws IOException {
        return getDiskCacheInstance(context).remove(key);
    }

    /**
     * get file
     *
     * @param context
     * @param key
     * @return
     */
    public static File getFile(Context context, String key) {
        return new File(getDiskCachePath(context) + File.separator + md5(key) + ".0");
    }

    /**
     * get temp file with the real file name(server)
     * should delete  (call deleteTempDir) the temp dir when the action done
     *
     * @param context
     * @param key
     * @param temdirname
     * @param filename
     * @return
     */
    public static File getTempFile(Context context, String key, String temdirname, String filename) {
        File file = null;
        File dirFile = new File(getDiskCacheDir(context) + File.separator + temdirname);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        try {
            InputStream is = getDiskCacheInstance(context).getInputStream(key).getInputStream();
            file = new File(dirFile.getPath() + File.separator + filename);
            FileOutputStream os = new FileOutputStream(file);
            IOUtils.copy(is, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * delete temp dir
     *
     * @param context
     * @param path
     * @param temdirname
     */
    public static void deleteTempDir(@NonNull Context context, String path, @NonNull String temdirname) {
        File dirFile;
        if (TextUtils.isEmpty(path)) {
            dirFile = new File(getDiskCacheDir(context) + File.separator + temdirname);
        } else {
            dirFile = new File(path);
        }
        if (dirFile != null && dirFile.exists()) {
            File[] files = dirFile.listFiles();
            if (files.length == 0) {
                dirFile.delete();
                return;
            } else {
                for (int index = 0; index < files.length; index++) {
                    if (files[index].isDirectory()) {
                        deleteTempDir(context, files[index].getPath(), temdirname);
                    } else {
                        files[index].delete();
                    }
                }
            }
            dirFile.delete();
        }
    }

    /**
     * @param cache
     * @param key
     * @return
     */
    public static boolean isExist(SimpleDiskCache cache, String key) {
        try {
            return cache.contains(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * get disk cache dir file
     *
     * @param context
     * @return
     */
    private static File getDiskCacheDir(Context context) {
        return new File(getDiskCachePath(context));
    }

    /**
     * get disk cache dir path
     *
     * @param context
     * @return
     */
    public static String getDiskCachePath(Context context) {
        String cachePath;
        if (isHasStoragePermission(context)) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                if (context.getExternalCacheDir() != null) {
                    cachePath = context.getExternalCacheDir().getPath();
                } else {
                    cachePath = context.getCacheDir().getPath();
                }
            } else {
                cachePath = context.getCacheDir().getPath();
            }
            return cachePath + File.separator + DISKCACHE_DIR_FILES_SD;
        } else {
            cachePath = context.getCacheDir().getPath();
            return cachePath + File.separator + DISKCACHE_DIR_FILES_CACHE;
        }
    }

    /**
     * get file count in disk
     *
     * @param context
     * @return
     */
    public static int getFileCountInDisk(Context context) {
        File f = getDiskCacheDir(context);
        if (f.isDirectory()) {
            return f.listFiles().length;
        }
        return 0;
    }

    /**
     * get app version
     *
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static String md5(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes("UTF-8"));
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }

    private static boolean isHasStoragePermission(Context context) {
        boolean hasStoragePermission = true;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            hasStoragePermission = false;
        }
        return hasStoragePermission;
    }

}
