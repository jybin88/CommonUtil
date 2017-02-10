package com.lfh.custom.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.ContextThemeWrapper;

import java.lang.reflect.Field;

/**
 * 获取状态高度、屏幕高度、屏幕宽度
 * Created by lifuhai on 2017/1/19 0019.
 */
public class ScreenUtil {

    public ScreenUtil() {
        /* no-op */
    }

    /**
     * 获取状态栏高度
     *
     * @param pActivity activity
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Activity pActivity) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = pActivity.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusBarHeight;
    }

    /**
     * 获取ActionBar高度
     * @param pWrapper {@link android.view.ContextThemeWrapper}
     * @return ActionBar高度
     */
    public static int getActionBarSize(ContextThemeWrapper pWrapper) {
        int[] actionBarSizeAttr = new int[]{android.R.attr.actionBarSize};
        TypedArray a = pWrapper.getTheme().obtainStyledAttributes(actionBarSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(0, -1);
        a.recycle();

        return actionBarSize;
    }

    /**
     * 屏幕高度
     *
     * @param pContext context
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context pContext) {
        return pContext.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 屏幕宽度
     *
     * @param pContext context
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context pContext) {
        return pContext.getResources().getDisplayMetrics().widthPixels;
    }
}
