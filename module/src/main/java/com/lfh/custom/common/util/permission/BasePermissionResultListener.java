package com.lfh.custom.common.util.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * 权限申请失败后默认的弹窗
 */
public abstract class BasePermissionResultListener implements OnPermissionResultListener{

    @Override
    public void onFailure(final Activity activity) {
        getBuilder(activity).show();
    }

    //点击去应用设置界面后的操作
    public void toSettingActivity() {

    }
    //弹出的dialog点击取消后的操作，由用户自己去实现
    public void onCancel() {

    }

    private MaterialDialog.Builder getBuilder(final Activity activity) {
        MaterialDialog.Builder builder = CommonUtils.createFailureDialog(activity, new CommonUtils.FailureDialogCallback() {
            @Override
            public void onCancelDialog(MaterialDialog dialog) {
                onCancel();
            }

            @Override
            public void onGoToSettingActivity(MaterialDialog dialog) {
                CommonUtils.startIntent(activity);
                toSettingActivity();
            }
        });
        return builder;
    }
}
