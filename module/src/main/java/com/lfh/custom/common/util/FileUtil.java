package com.lfh.custom.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import log.KLog;

/**
 * Created by lifuhai on 2017/2/10 0010.
 */
public final class FileUtil {
    private static final String TAG = "FileUtil";

    private FileUtil() {
        /* no-op */
    }

    /**
     * 从Uri中获取图片真实地址
     *
     * @param pContext {@link Context}
     * @param pUri     {@link Uri}
     * @return 图片真实地址
     */
    public static String getRealPathFromUri(Context pContext, Uri pUri) {
        String realPath = "";

        if (null != pUri) {
            final String scheme = pUri.getScheme();

            if (scheme == null) {
                realPath = pUri.getPath();
            } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
                realPath = pUri.getPath();
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                Cursor cursor = pContext.getContentResolver().query(pUri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);

                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

                        if (index > -1) {
                            realPath = cursor.getString(index);
                        }
                    }

                    cursor.close();
                }
            }
        }

        return realPath;
    }

    /**
     * 获取文件二进制
     *
     * @param pFilePath 路径
     */
    public byte[] getFileBytes(String pFilePath) {
        byte[] buffer = null;

        try {
            File file = new File(pFilePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
            byte[] bytes = new byte[1000];
            int index = 0;
            while (index != -1) {
                byteArrayOutputStream.write(bytes, 0, index);
                index = fileInputStream.read(bytes);
            }
            fileInputStream.close();
            byteArrayOutputStream.close();
            buffer = byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer;
    }

    /**
     * 获取应用专属缓存目录
     * android 4.4及以上系统不需要申请SD卡读写权限
     * 因此也不用考虑6.0系统动态申请SD卡读写权限问题，且随应用被卸载后自动清空 不会污染用户存储空间
     *
     * @param pContext 上下文
     * @param pType    文件夹类型 可以为空，为空则返回API得到的一级目录
     *                 {@link android.os.Environment#DIRECTORY_MUSIC},
     *                 {@link android.os.Environment#DIRECTORY_PODCASTS},
     *                 {@link android.os.Environment#DIRECTORY_RINGTONES},
     *                 {@link android.os.Environment#DIRECTORY_ALARMS},
     *                 {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *                 {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *                 {@link android.os.Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
     */
    public File getCacheDirectory(Context pContext, String pType) {
        if (null == pContext) {
            KLog.i(TAG, "getCacheDirectory fail, the reason is context is null");
            return null;
        }

        File appCacheDir = getExternalCacheDirectory(pContext, pType);

        if (null == appCacheDir) {
            appCacheDir = getInternalCacheDirectory(pContext, pType);
        }

        if (null == appCacheDir) {
            KLog.i(TAG, "getCacheDirectory fail, the reason is mobile phone unknown exception !");
            return null;
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            KLog.i(TAG, "getCacheDirectory fail, the reason is make directory fail !");
            return null;
        }

        return appCacheDir;
    }

    /**
     * 获取内存缓存目录
     *
     * @param pContext 上下文
     * @param pType    子目录，可以为空，为空直接返回一级目录
     *                 {@link android.os.Environment#DIRECTORY_MUSIC},
     *                 {@link android.os.Environment#DIRECTORY_PODCASTS},
     *                 {@link android.os.Environment#DIRECTORY_RINGTONES},
     *                 {@link android.os.Environment#DIRECTORY_ALARMS},
     *                 {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *                 {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *                 {@link android.os.Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    private File getInternalCacheDirectory(Context pContext, String pType) {
        File appCacheDir;

        if (TextUtils.isEmpty(pType)) {
            appCacheDir = pContext.getCacheDir();  // /data/data/app_package_name/cache
        } else {
            appCacheDir = new File(pContext.getFilesDir(), pType); // /data/data/app_package_name/files/pType
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdir()) {
            KLog.i(TAG, "getInternalDirectory fail, the reason is make directory fail !");
            return null;
        }

        return appCacheDir;
    }

    /**
     * 获取SD卡缓存目录
     *
     * @param pContext 上下文
     * @param pType    文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *                 否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为 .../data/app_package_name/files/Pictures
     *                 {@link android.os.Environment#DIRECTORY_MUSIC},
     *                 {@link android.os.Environment#DIRECTORY_PODCASTS},
     *                 {@link android.os.Environment#DIRECTORY_RINGTONES},
     *                 {@link android.os.Environment#DIRECTORY_ALARMS},
     *                 {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *                 {@link android.os.Environment#DIRECTORY_PICTURES}, or
     *                 {@link android.os.Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    private File getExternalCacheDirectory(Context pContext, String pType) {
        File appCacheDir;

        if (!TextUtils.equals(Environment.MEDIA_MOUNTED, Environment.getExternalStorageState())) {
            KLog.e(TAG, "getExternalCacheDirectory fail, the reason is sdCard nonexistence or sdCard mount fail !");
            return null;
        }

        if (TextUtils.isEmpty(pType)) {
            appCacheDir = pContext.getExternalCacheDir();
        } else {
            appCacheDir = pContext.getExternalFilesDir(pType);
        }

        if (null == appCacheDir) {// 有些手机需要通过自定义目录
            appCacheDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + pContext.getPackageName() + "/cache/" + pType);
        }

        if (null == appCacheDir) {
            KLog.e(TAG, "getExternalCacheDirectory fail, the reason is sdCard unknown exception !");
            return null;
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            KLog.e(TAG, "getExternalCacheDirectory fail, the reason is sdCard nonexistence or sdCard mount fail !");
            return null;
        }

        return appCacheDir;
    }
}
