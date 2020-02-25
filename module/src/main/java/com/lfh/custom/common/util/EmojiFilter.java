package com.lfh.custom.common.util;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * EditText emoji过滤器
 *
 * @author lifuhai@linkcircle.cn
 * @date 2020/2/25 17:23
 */
public class EmojiFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            int type = Character.getType(source.charAt(i));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return "";
            }
        }

        return null;
    }
}
