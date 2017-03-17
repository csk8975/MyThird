package com.detection.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @fileName DateUtil.java
 * @author csk
 * @createTime 2017年3月2日 下午5:28:45
 * @version 1.0
 * @function
 */

public class DateUtil {

    public static String getYearMonthDateByHyphen(Date date) {
        if(date == null){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd ");

        return sdf.format(date);
    }

    public static String getYearMonthDateByChinese(Date date) {
        if(date == null){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy年MM月dd日 ");

        return sdf.format(date);
    }

}
