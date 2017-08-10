package com.lfh.custom.common.util.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;

public class PermissionResultActivity extends Activity implements CommonUtils.FailureDialogCallback, DialogInterface.OnDismissListener {
    private static final HashMap<String, BasePermissionClient> CLIENT_TEMP_MAP = new HashMap<>();

    private BasePermissionClient mClient;

    public static void startActivity(Context context, BasePermissionClient client) {
        if (client == null) {
            return;
        }
        String clientId = SystemClock.elapsedRealtime() + "_" + client.hashCode();
        CLIENT_TEMP_MAP.put(clientId, client);
        Intent intent = new Intent(context, PermissionResultActivity.class);
        intent.putExtra("clientId", clientId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String clientId = getIntent().getStringExtra("clientId");
        mClient = CLIENT_TEMP_MAP.remove(clientId);
        if (mClient == null) {
            finish();
            return;
        }
        CommonUtils.createFailureDialog(this, this)
                .show()
                .setOnDismissListener(this);
    }

    @Override
    public void onCancelDialog(MaterialDialog dialog) {
        if (mClient != null) {
            mClient.onCancel();
        }
    }

    @Override
    public void onGoToSettingActivity(MaterialDialog dialog) {
        if (mClient != null) {
            mClient.onGoToSettingActivity(this);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
