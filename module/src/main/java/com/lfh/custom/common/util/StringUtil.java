package com.lfh.custom.common.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String 工具类
 * Created by Jason on 2017/8/27 9:32.
 */
public class StringUtil {
    /**
     * 是否是有效的手机号码
     *
     * @param pPhone 手机号码
     * @return true false
     */
    public static boolean isPhoneNumberValid(String pPhone) {
        if (TextUtils.isEmpty(pPhone)) {
            return false;
        }

        boolean isValid = false;
        String expression = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(14[^[1-4]]))\\d{8}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(pPhone);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    /**
     * 是否是有效的邮箱
     *
     * @param pEmail 邮箱
     * @return true false
     */
    public static boolean isEmailValid(String pEmail) {
        if (TextUtils.isEmpty(pEmail)) {
            return false;
        }

        boolean isValid = false;
        String expression = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(pEmail);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    /**
     * 是否全数字
     *
     * @param pValue 字符串
     * @return true false
     */
    public static boolean isNumeric(String pValue) {
        if (TextUtils.isEmpty(pValue)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(pValue).matches();
    }

    /**
     * 字符串转成utf8编码
     *
     * @param pValue 字符
     * @return utf8编码字符串
     */
    public static String convertStringToUtf8(String pValue) {
        if (TextUtils.isEmpty(pValue)) {
            return "";
        }

        String newValue = pValue.replace("\n", "");

        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (UnsupportedEncodingException pE) {
                    pE.printStackTrace();
                }
            }
        }

        return newValue;
    }

    /**
     * utf8编码转成String
     *
     * @param pUtf8String utf8编码字符串
     * @return 转换后的字符串
     */
    public static String convertUft8ToString(String pUtf8String) {
        String newString;

        try {
            newString = URLDecoder.decode(pUtf8String, "UTF-8");
        } catch (UnsupportedEncodingException pE) {
            newString = pUtf8String;
        }

        return newString;
    }
}
