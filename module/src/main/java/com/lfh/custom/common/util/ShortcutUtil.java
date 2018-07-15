package com.lfh.custom.common.util;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * 快捷方式工具类
 * Created by lifuhai on 2017/2/10 0010.
 */
public final class ShortcutUtil {
    private static final String TAG = "ShortcutUtil";

    private ShortcutUtil() {
        /* no-op */
    }

    /**
     * 是否已创建过快捷方式
     *
     * @param pContext      {@link Context}
     * @param pShortcutName 快捷方式名称
     * @return true 已创建 false 没创建
     */
    @SuppressLint("Recycle")
    private static boolean hasShortcut(Context pContext, String pShortcutName) {
        boolean isInstallShortcut = false;
        final String AUTHORITY = getSettingsPermissionAuthority(pContext);
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = pContext.getContentResolver().query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{pShortcutName}, null);

        if (null != c && c.getCount() > 0) {
            isInstallShortcut = true;
        }

        return isInstallShortcut;
    }

    /**
     * 创建快捷方式
     *
     * @param pContext      {@link Context}
     * @param pShortcutName 快捷方式名称
     * @param pIntent       快捷方式打开的Intent
     */
    public static void createShortcut(Context pContext, String pShortcutName, Intent pIntent) {
        createShortcut(pContext, pShortcutName, pIntent, null);
    }

    /**
     * 创建快捷方式
     *
     * @param pContext      {@link Context}
     * @param pShortcutName 快捷方式名称
     * @param pIntent       快捷方式打开的Intent
     * @param pShortcutIcon 快捷方式图标
     */
    public static void createShortcut(Context pContext, String pShortcutName, Intent pIntent, Bitmap pShortcutIcon) {
        if (hasShortcut(pContext, pShortcutName)) {
            Log.d(TAG, "快捷方式已经存在");
            return;
        }

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, pShortcutName);
        //不允许重复创建
        shortcut.putExtra("duplicate", false);
        //快捷方式的图标，可以添加两种类型
        if (null != pShortcutIcon) {
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, pShortcutIcon);//有图片就用图片，没有就用当前App图片
        } else {
            Drawable drawable = AppUtil.getAppIcon(pContext);

            if (null != drawable) {
                shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, ((BitmapDrawable) drawable).getBitmap());
            }
        }

        // 指定当前的Activity为快捷方式启动的对象
        ComponentName comp = new ComponentName(pContext.getPackageName(), pContext.getClass().getName());
        pIntent.setComponent(comp);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, pIntent);
        pContext.sendBroadcast(shortcut);
    }

    private static String getSettingsPermissionAuthority(Context context) {
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        final String readPermission = provider.readPermission;
                        final String writePermission = provider.writePermission;
                        if (readPermission != null && writePermission != null) {
                            final boolean readPermissionMatches = readPermission.endsWith(".permission.READ_SETTINGS");
                            final boolean writePermissionMatches = writePermission.endsWith(".permission.WRITE_SETTINGS");
                            if (readPermissionMatches && writePermissionMatches) {
                                // And if we found the right one we return the authority
                                return provider.authority;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
