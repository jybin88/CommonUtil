package com.lfh.custom.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author lifuhai
 * @date 2017/2/10
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

    /**
     * 获取进程名
     *
     * @param pProcessId 进程id  通过android.os.Process.myPid()获得
     * @return 进程名
     */
    public static String getProcessName(int pProcessId) {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/" + pProcessId + "/cmdline"));
            String processName = bufferedReader.readLine();

            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }

            return processName;
        } catch (Throwable pThrowable) {
            pThrowable.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException pE) {
                pE.printStackTrace();
            }
        }

        return null;
    }
}
