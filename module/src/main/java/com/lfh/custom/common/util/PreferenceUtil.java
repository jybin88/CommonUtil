package com.lfh.custom.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Preference 工具类
 *
 * @author Jason
 * @date 2017/2/13
 */
public class PreferenceUtil {
    private String mPreferenceName = "preference";
    private static PreferenceUtil sInstance;

    public PreferenceUtil() {
        /* no-op */
    }

    private PreferenceUtil(String pFileName) {
        mPreferenceName = pFileName;
    }

    public static PreferenceUtil getInstance(String pFileName) {
        if (null == sInstance) {
            synchronized (PreferenceUtil.class) {
                if (null == sInstance) {
                    sInstance = new PreferenceUtil(pFileName);
                }
            }
        }

        return sInstance;
    }

    public String read(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.getString(key, defaultValue);
    }

    public boolean write(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.edit().putString(key, value).commit();
    }

    public int read(Context pContext, String pKey, int pDefaultValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.getInt(pKey, pDefaultValue);
    }


    public boolean write(Context pContext, String pKey, int pValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.edit().putInt(pKey, pValue).commit();

    }

    public boolean read(Context pContext, String pKey, boolean pDefaultValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.getBoolean(pKey, pDefaultValue);
    }

    public boolean write(Context pContext, String pKey, boolean pValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.edit().putBoolean(pKey, pValue).commit();
    }

    public long read(Context pContext, String pKey, long pDefaultValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.getLong(pKey, pDefaultValue);
    }

    public boolean write(Context pContext, String pKey, long pValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.edit().putLong(pKey, pValue).commit();
    }
    
    public float read(Context pContext, String pKey, float pDefaultValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.getFloat(pKey, pDefaultValue);
    }

    public boolean write(Context pContext, String pKey, float pValue) {
        SharedPreferences sp = pContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);

        return sp.edit().putFloat(pKey, pValue).commit();
    }
}
