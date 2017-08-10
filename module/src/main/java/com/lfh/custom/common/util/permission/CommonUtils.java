package com.lfh.custom.common.util.permission;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lfh.custom.common.util.R;

public class CommonUtils {

    public static MaterialDialog.Builder createFailureDialog(Activity activity, final FailureDialogCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        //// TODO: 2017/6/15 skd23以上以及非标准的sdk23以下的可以弹出正常的dialog，其余弹出异常dialog。
        if (isContainExceptionBrand()) {
            builder.title(activity.getResources().getString(R.string.permissioncheck_exception_title))
                    .content(activity.getResources().getString(R.string.permissioncheck_exception_content))
                    .negativeText(activity.getResources().getString(R.string.permissioncheck_exception_close))
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            callback.onCancelDialog(dialog);
                        }
                    });
        } else {
            builder.title(activity.getResources().getString(R.string.permissioncheck_title))
                    .content(activity.getResources().getString(R.string.permissioncheck_content))
                    .positiveText(activity.getResources().getString(R.string.permissioncheck_gosetting))
                    .negativeText(activity.getResources().getString(R.string.permissioncheck_cancle))
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which == DialogAction.POSITIVE) {
                                callback.onGoToSettingActivity(dialog);
                            } else if (which == DialogAction.NEGATIVE) {
                                dialog.dismiss();
                                callback.onCancelDialog(dialog);
                            }
                        }
                    });
        }
        return builder;
    }

    public static boolean isContainExceptionBrand() {
        boolean isExceptionBrand = false;
        if (Build.BRAND.contains("Xiaomi")) {
            isExceptionBrand = true;
        } else if (Build.BRAND.contains("OPPO")) {
            isExceptionBrand = true;
        }
        return isExceptionBrand;
    }

    //跳转到应用设置界面
    public static void startIntent(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
            if(!(context instanceof Activity)){
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public interface FailureDialogCallback {

        void onCancelDialog(MaterialDialog dialog);

        void onGoToSettingActivity(MaterialDialog dialog);
    }
}
