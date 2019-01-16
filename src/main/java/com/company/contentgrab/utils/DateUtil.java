package com.company.contentgrab.utils;

import java.time.LocalDate;

public class DateUtil {

    /**
     *  判断是否是当天
     * @param str 格式为yyyy-mm-dd的时间字符串
     * @return
     */
    public static boolean isToday(String str){
        LocalDate lld1 = LocalDate.now();
        LocalDate lld2 = LocalDate.parse(str);
        if (lld1.equals(lld2)) {
            return true;
        }
        return false;
    }

//    public static boolean compareTo
}
