package com.lfh.custom.common.util.permission;

import android.app.Activity;

public interface OnPermissionResultListener {

    void onSuccess(Activity activity);

    void onFailure(Activity activity);

}
