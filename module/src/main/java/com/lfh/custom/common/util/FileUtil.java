package com.lfh.custom.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by lifuhai on 2017/2/10 0010.
 */
public class FileUtil {

    public FileUtil() {
        /* no-op */
    }
    
    /**
     *   * 获取cache路径
     *   *
     *   * @param pContext 上下文
     *   * @return cache路径
     *   
     */
    private static String getDiskCachePath(Context pContext) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            //存在外部sdcard
            File cacheDir = pContext.getExternalCacheDir();

            if (null != cacheDir) {//不为空
                cachePath = cacheDir.getPath();
            }
        } else {//不存在外部sdcard
            cachePath = pContext.getCacheDir().getPath();
        }

        if (!TextUtils.isEmpty(cachePath) && !cachePath.endsWith("/")) {//保证以"/"结尾
            cachePath = cachePath + "/";
        }

        return cachePath;
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
}
