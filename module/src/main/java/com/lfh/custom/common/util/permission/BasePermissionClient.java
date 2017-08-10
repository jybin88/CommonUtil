package com.lfh.custom.common.util.permission;

import android.app.Activity;
import android.content.Context;

public abstract class BasePermissionClient implements IPermissionClient {
    @Override
    public void onFailure(Context context) {
        PermissionResultActivity.startActivity(context, this);
    }

    //点击去应用设置界面后的操作
    public void onGoToSettingActivity(Activity activity) {
        CommonUtils.startIntent(activity);
    }
    //弹出的dialog点击取消后的操作，由用户自己去实现
    public void onCancel() {
    }
}
