package com.lfh.custom.common.util;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
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

        if (0 == statusBarHeight) {
            //前面方法获取不到值重新获取一次
            Resources resources = Resources.getSystem();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
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
     * @deprecated {@see getNavigationBarHeight()}
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
     * 虚拟导航键是否打开
     *
     * @param context 上下文
     * @return true 打开 false 未打开
     */
    public static boolean navigationBarEnable(Context context) {
        if (0 == getNavigationBarHeight(context)) {
            return false;
        }

        if (navigationGestureEnabled(context)) {
            return false;
        }

        return isHasNavigationBar(context);
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

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method m = c.getDeclaredMethod("get", String.class);
            m.setAccessible(true);
            navigationBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return navigationBarOverride;
    }

    /**
     * 判断手势导航是否开启
     *
     * @param context 上下文
     * @return true 表示使用的是手势，false 表示使用的是虚拟导航键(NavigationBar)， 默认是false
     */
    private static boolean navigationGestureEnabled(Context context) {
        return 0 != Settings.Secure.getInt(context.getContentResolver(), getNavigationGlobalInfo(), 0);
    }

    /**
     * 获取手机设置中的"navigation_gesture_on"值（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo、三星都可以）
     *
     * @return "navigation_gesture_on"值
     */
    private static String getNavigationGlobalInfo() {
        if (RomUtil.isHuawei() || RomUtil.isHonor()) {
            return "navigationbar_is_min";
        } else if (RomUtil.isXiaomi()) {
            return "force_fsg_nav_bar";
        } else if (RomUtil.isVivo()) {
            return "navigation_gesture_on";
        } else if (RomUtil.isOppo()) {
            return "hide_navigationbar_enable";
        } else if (RomUtil.isSamsung()) {
            return "navigationbar_hide_bar_enabled";
        } else if (RomUtil.isNubia()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                return "navigation_bar_can_hiden";
            } else {
                return "swipe_up_to_switch_apps_enabled";
            }
        }

        return "navigationbar_is_min";
    }

    /**
     * 根据屏幕真实高度与显示高度，判断虚拟导航栏是否显示
     * @return true 表示虚拟导航栏显示，false 表示虚拟导航栏未显示
     */
    private static boolean isHasNavigationBar(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        display.getRealMetrics(realDisplayMetrics);
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        // 部分无良厂商的手势操作，显示高度 + 导航栏高度，竟然大于物理高度，对于这种情况，直接默认未启用导航栏
        if (displayHeight > displayWidth) {
            if (displayHeight + ScreenUtil.getNavigationBarHeight(context) > realHeight) {
                return false;
            }
        } else {
            if (displayWidth + ScreenUtil.getNavigationBarHeight(context) > realWidth) {
                return false;
            }
        }

        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0;
    }
}
