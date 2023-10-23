package com.example.poslj.utils;

import android.text.TextUtils;

/**
 * 作者: qgl
 * 创建日期：2023/4/28
 * 描述:数字加密处理
 */
public class NumberUtil {

    /**
     * 手机号加密
     * @param phone
     * @return
     */
    public static String isPhoneEnc(String phone) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(phone) && phone.length() > 6) {
            for (int i = 0; i < phone.length(); i++) {
                char c = phone.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String isBankNo(String num){
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(num) && num.length() > 13) {
            for (int i = 0; i < num.length(); i++) {
                char c = num.charAt(i);
                if (i >= 4 && i <= 12) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
} 