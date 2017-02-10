package com.lfh.custom.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * dp、sp、px间的转化
 * Created by lifuhai on 2017/1/19 0019.
 */
public class DimenUtil {

    public DimenUtil() {
        /* no-op */
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param pContext {@link android.content.Context}
     * @param pDpValue dp值
     * @return px值
     */
    public static int dip2px(Context pContext, float pDpValue) {
        DisplayMetrics metrics = pContext.getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pDpValue, metrics);

        return (int) px;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param pContext {@link android.content.Context}
     * @param pDpRes   dp resource
     * @return px值
     */
    public static int dip2px(Context pContext, @DimenRes int pDpRes) {
        Resources r = pContext.getResources();

        return (int) r.getDimension(pDpRes);
    }

    /**
     * 根据手机的分辨率从 px 的单位 转成为 dp
     *
     * @param pContext {@link android.content.Context}
     * @param pPxValue px 值
     * @return dip值
     */
    public static int px2dip(Context pContext, float pPxValue) {
        final float scale = pContext.getResources().getDisplayMetrics().density;

        return (int) (pPxValue / scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     *
     * @param pContext {@link android.content.Context}
     * @param pSpValue sp值
     * @return px值
     */
    public static int sp2px(Context pContext, float pSpValue) {
        DisplayMetrics metrics = pContext.getResources().getDisplayMetrics();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pSpValue, metrics);

        return (int) px;
    }
}
