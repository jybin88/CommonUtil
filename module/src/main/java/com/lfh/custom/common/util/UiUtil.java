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
public class UiUtil {
    public UiUtil() {
        /* no-op */
    }

    public static String getAuthorityFromPermission(Context context) {
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

    /**
     * 获取APP图标
     *
     * @param pContext {@link Context}
     * @return APP图标
     */
    public static Drawable getAppIcon(Context pContext) {
        List<PackageInfo> packageInfos = pContext.getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo packageInfo = packageInfos.get(i);

            if (packageInfo.packageName.equals(pContext.getPackageName())) {
                return packageInfo.applicationInfo.loadIcon(pContext.getPackageManager());
            }
        }

        return null;
    }
}
