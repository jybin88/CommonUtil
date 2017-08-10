package com.lfh.custom.common.util.permission;

import android.content.Context;

public interface IPermissionClient {
    void onSuccess(Context context);

    void onFailure(Context context);
}
