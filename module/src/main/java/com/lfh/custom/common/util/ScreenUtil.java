package com.lfh.custom.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 获取状态高度、屏幕高度、屏幕宽度
 * Created by lifuhai on 2017/1/19 0019.
 */
@SuppressWarnings("unchecked")
public class ScreenUtil {

    public ScreenUtil() {
        /* no-op */
    }

    /**
     * 获取状态栏高度
     *
     * @param pContext 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context pContext) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = pContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusBarHeight;
    }

    /**
     * 获取ActionBar高度
     *
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

    /**
     * 获取虚拟功能键高度
     *
     * @param pContext 上下文
     * @return 虚拟功能键高度
     */
    public static int getVirtualBarHeight(Context pContext) {
        int virtualBarHeight = 0;
        WindowManager windowManager = (WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE);

        if (null == windowManager) {
            return 0;
        }

        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        try {
            Class clazz = Class.forName("android.view.Display");
            Method method = clazz.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            virtualBarHeight = displayMetrics.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception pE) {
            pE.printStackTrace();
        }

        return virtualBarHeight;
    }

    /**
     * 获取虚拟功能键高度
     *
     * @param pActivity Activity
     * @return 虚拟功能键高度
     */
    public static int getVirtualBarHeight(Activity pActivity) {
        int titleHeight = 0;
        Rect frame = new Rect();
        pActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        titleHeight = pActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() - statusHeight;

        return titleHeight;
    }
}
