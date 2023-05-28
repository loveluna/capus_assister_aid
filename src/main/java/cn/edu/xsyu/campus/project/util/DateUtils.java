package cn.edu.xsyu.campus.project.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式(yyyyMMdd)
     */
    public final static String DATEPATTERN = "yyyyMMdd";

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 计算距离当前时间的时间差，并返回相应的时间格式
     */
    public static String getCreateTimeDesc(Date createTime) {
        long createTimeMillis = createTime.getTime();
        long nowMillis = System.currentTimeMillis();
        long diffMillis = nowMillis - createTimeMillis;
        long diffSeconds = diffMillis / 1000;
        if (diffSeconds < 60) {
            return "刚刚";
        } else if (diffSeconds < 60 * 60) {
            return diffSeconds / 60 + "分钟前";
        } else if (diffSeconds < 60 * 60 * 24) {
            return diffSeconds / (60 * 60) + "小时前";
        } else {
            return diffSeconds / (60 * 60 * 24) + "天前";
        }
    }

    public static String formatTime(long timestamp) {
        Date now = new Date();
        Date createTime = new Date(timestamp);
        long diff = now.getTime() - createTime.getTime();
        long minute = 60 * 1000;
        long hour = 60 * minute;
        long day = 24 * hour;

        if (diff < minute) {
            return "刚刚";
        } else if (diff < hour) {
            return diff / minute + "分钟前";
        } else {
            Calendar nowCalendar = Calendar.getInstance();
            Calendar createCalendar = Calendar.getInstance();
            createCalendar.setTime(createTime);
            SimpleDateFormat dateFormat;
            if (nowCalendar.get(Calendar.YEAR) == createCalendar.get(Calendar.YEAR)) {
                if (nowCalendar.get(Calendar.DAY_OF_YEAR) == createCalendar.get(Calendar.DAY_OF_YEAR)) {
                    dateFormat = new SimpleDateFormat("今天 HH:mm");
                } else {
                    dateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
                }
            } else {
                dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            }
            return dateFormat.format(createTime);
        }
    }
}
