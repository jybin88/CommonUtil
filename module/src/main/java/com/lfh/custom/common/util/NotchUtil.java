package com.lfh.custom.common.util;

import android.content.Context;

import java.lang.reflect.Method;

import log.KLog;

/**
 * 刘海屏幕工具类
 * <p>
 * Created by Jason on 2018/7/29 9:56.
 */
public class NotchUtil {
    private static final String TAG = "NotchUtil";
    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海
    public static final int VIVO_FILLET = 0x00000008;//是否有圆角

    /**
     * 华为手机是否有刘海
     *
     * @param context 上下文
     * @return true false
     */
    public static boolean hasNotchAtHuawei(Context context) {
        boolean hasNotch = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            hasNotch = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            KLog.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            KLog.e(TAG, e.getMessage());
        } catch (Exception e) {
            KLog.e(TAG, e.getMessage());
        }

        return hasNotch;
    }

    /**
     * 获取华为刘海尺寸
     *
     * @param context 上下文
     * @return 刘海尺寸  int[0] 宽度 int[1] 高度
     */
    public static int[] getNotchSizeAtHuawei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            KLog.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            KLog.e(TAG, e.getMessage());
        } catch (Exception e) {
            KLog.e(TAG, e.getMessage());
        }

        return ret;
    }

    /**
     * vivo 手机是否有刘海
     *
     * @param context 上下文
     * @return true false
     */
    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            KLog.e(TAG, e.getMessage());
        } catch (NoSuchMethodException e) {
            KLog.e(TAG, e.getMessage());
        } catch (Exception e) {
            KLog.e(TAG, e.getMessage());
        }

        return ret;
    }

    /**
     * oppo 手机是否有刘海
     *
     * @param context 上下文
     * @return true false
     */
    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
}
