package com.lfh.custom.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by lifuhai on 2017/2/10 0010.
 */
public final class AppUtil {
    private AppUtil() {
        /* no-op */
    }

    /**
     * 获取APP图标
     *
     * @param pContext {@link Context}
     * @return APP图标
     */
    public static Drawable getAppIcon(Context pContext) {
        List<PackageInfo> packageInfoList = pContext.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo packageInfo = packageInfoList.get(i);

            if (packageInfo.packageName.equals(pContext.getPackageName())) {
                return packageInfo.applicationInfo.loadIcon(pContext.getPackageManager());
            }
        }

        return null;
    }
}
