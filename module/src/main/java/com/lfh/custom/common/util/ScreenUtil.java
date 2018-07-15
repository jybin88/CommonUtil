package com.lfh.custom.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 获取状态高度、屏幕高度、屏幕宽度
 * Created by lifuhai on 2017/1/19 0019.
 */
@SuppressWarnings("unchecked")
@SuppressLint("PrivateApi")
public final class ScreenUtil {
    private static final String TAG = "ScreenUtil";

    private ScreenUtil() {
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
     * 屏幕高度包含虚拟状态栏
     *
     * @param pContext pContext
     * @return 屏幕高度包含虚拟状态栏
     */
    public static int getScreenHeightWithNavigationBar(Context pContext) {
        int screenHeight = 0;
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
            screenHeight = displayMetrics.heightPixels;
        } catch (Exception pE) {
            pE.printStackTrace();
        }

        return screenHeight;
    }

    /**
     * 获取虚拟功能键高度
     *
     * @param pContext 上下文
     * @return 虚拟功能键高度
     * @deprecated {@link com.lfh.custom.common.util.getNavigationBarHeight()}
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
     * @param pContext 上下文
     * @return 虚拟功能键高度
     */
    public static int getNavigationBarHeight(Context pContext) {
        int navigationBarHeight = 0;

        if (hasNavigationBar(pContext)) {
            Resources res = pContext.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");

            if (resourceId > 0) {
                navigationBarHeight = res.getDimensionPixelSize(resourceId);
            }

            if (0 == navigationBarHeight) {
                WindowManager windowManager = (WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE);

                if (null == windowManager) {
                    return 0;
                }
                navigationBarHeight = getScreenHeightWithNavigationBar(pContext) - windowManager.getDefaultDisplay().getHeight();
            }
        }

        return navigationBarHeight;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param pContext 上下文
     * @return true 有 false 无
     */
    private static boolean hasNavigationBar(Context pContext) {
        Resources resources = pContext.getResources();
        int resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");

        if (resourceId != 0) {
            boolean hasNavigation = resources.getBoolean(resourceId);
            // check override flag
            String navigationBarOverride = getNavigationBarOverride();

            if ("1".equals(navigationBarOverride)) {
                hasNavigation = false;
            } else if ("0".equals(navigationBarOverride)) {
                hasNavigation = true;
            }

            return hasNavigation;
        } else { // fallback
            return !ViewConfiguration.get(pContext).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return 虚拟按键重写标志
     */
    private static String getNavigationBarOverride() {
        String navigationBarOverride = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                navigationBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return navigationBarOverride;
    }
}
