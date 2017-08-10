package com.lfh.custom.common.util.permission;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class PermissionUtil {
    /**
     * 权限申请
     *
     * @param context
     * @param client    权限申请结果回调
     * @param permissions
     */
    public static void request(final Context context, final IPermissionClient client, final String... permissions) {
        if (client == null) {
            throw new IllegalArgumentException("client is null");
        }

        getPermissionObservable(context, permissions).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                    client.onSuccess(context);
                } else {
                    client.onFailure(context);
                }
            }
        });
    }

    /**
     * @param activity
     * @param onPermissionResultListener 权限申请结果回调
     * @param permissions
     */
    public static void request(final Activity activity, final OnPermissionResultListener onPermissionResultListener, final String... permissions) {
        if (onPermissionResultListener == null) {
            throw new IllegalArgumentException("onPermissionResultListener is null");
        }

        getPermissionObservable(activity, permissions).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) {
                    onPermissionResultListener.onSuccess(activity);
                } else {
                    onPermissionResultListener.onFailure(activity);
                }
            }
        });
    }

    /**
     * 判断系统版本是否是6.0及以上
     */
    private static boolean isSdkOver23() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * 判断sdk23以下的权限情况
     */
    private static boolean isGrant(Context context, String... permission) {
        boolean grantResult = true;
        for (String p : permission) {
            grantResult &= (PermissionChecker.checkSelfPermission(context, p) == PermissionChecker.PERMISSION_GRANTED);
        }
        return grantResult;
    }

    /**
     * 返回一个Observable，方便链式调用。在SDK>23以上通过RxPermission返回。否则通过PermissionChecker.checkSelfPermission()判断进行
     * 结果返回。
     *
     * @param context
     * @param permissions 需要申请的权限
     *
     * @return Observable
     */
    public static Observable<Boolean> getPermissionObservable(final Context context, final String... permissions) {
        if (isSdkOver23()) {
            return RxPermissions.getInstance(context).request(permissions).flatMap(new Func1<Boolean, Observable<Boolean>>() {
                @Override
                public Observable<Boolean> call(Boolean granted) {
                    return Observable.just(checkIsRealTrue(granted, context, permissions));
                }
            });

        } else {
            return Observable.just(isGrant(context, permissions));
        }
    }

    /**
     * 权限授予成功回调,但是目前发现小米/oppo等有些手机的位置权限并没遵循Google标准，
     * 拒绝后仍然会返回TRUE。因此需要进行二次判断。
     *
     * @param hasPermission
     */
    private static boolean checkIsRealTrue(Boolean hasPermission, final Context context, final String... permissions) {
        if (hasPermission) {
            if (CommonUtils.isContainExceptionBrand()) {
                if (isGrant(context, permissions)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return hasPermission;
            }
        } else {
            return false;
        }
    }
}
