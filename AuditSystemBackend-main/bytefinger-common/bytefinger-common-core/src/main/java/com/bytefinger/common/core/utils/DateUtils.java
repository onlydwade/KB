package com.bytefinger.common.core.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 *
 * @author patrick
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 加减时间
     * * @return Date() 当前日期
     *
     * @return day 加减日
     */
    public static Date addAndSubtractDate(Date date, Integer year, Integer month, Integer day) {
        //获取时间加一年或加一月或加一天
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); //设置起时间
        if (ObjectUtil.isNotEmpty(year)) {
            cal.add(Calendar.YEAR, year); //加减年
        }
        if (ObjectUtil.isNotEmpty(month)) {
            cal.add(Calendar.MONTH, month); //加减月
        }
        if (ObjectUtil.isNotEmpty(day)) {
            cal.add(Calendar.DATE, day); //加减日
        }
        return cal.getTime();
    }

    public static int month(Date date) {
        return DateUtil.month(date);
    }

    public static int year(Date date) {
        return DateUtil.year(date);
    }

    public static int getMonthSpace(Date startTime, Date endTime) {

        int result = 0;

        if ((DateUtils.dateTime(DateUtils.YYYY, DateUtils.parseDateToStr(DateUtils.YYYY, endTime)).compareTo(DateUtils.dateTime(DateUtils.YYYY, DateUtils.parseDateToStr(DateUtils.YYYY, new Date())))) > 0) {
            GregorianCalendar g = new GregorianCalendar();
            g.set(Calendar.YEAR, Integer.valueOf(DateUtils.parseDateToStr(DateUtils.YYYY, new Date())));
            g.set(Calendar.MONTH, 11);
            g.set(Calendar.DATE, 28);
            endTime = g.getTime();
        }
        if ((DateUtils.dateTime(DateUtils.YYYY, DateUtils.parseDateToStr(DateUtils.YYYY, startTime)).compareTo(DateUtils.dateTime(DateUtils.YYYY, DateUtils.parseDateToStr(DateUtils.YYYY, new Date())))) < 0) {
            GregorianCalendar g = new GregorianCalendar();
            g.set(Calendar.YEAR, Integer.valueOf(DateUtils.parseDateToStr(DateUtils.YYYY, new Date())));
            g.set(Calendar.MONTH, 0);
            g.set(Calendar.DATE, 1);
            startTime = g.getTime();
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(startTime);
        c2.setTime(endTime);

        //相差的月份
        result = (c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH)) + 1;

        return result;

    }


    public static int monthsToEndOfYear(Date date) {

        // 将 Date 转换为 ZonedDateTime，指定系统默认时区
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());

        // 从 ZonedDateTime 中提取 LocalDate
        LocalDate currentDate = zonedDateTime.toLocalDate();

        // 获取传入时间的月份
        int currentMonth = currentDate.getMonthValue();

        // 计算到年底的月份数（包含当前月）

        return 12 - currentMonth + 1;
    }

    public static Integer getCoveredMonths(Date serviceBeginTime, Date serviceEndTime, Date performanceConfirmTime) {
        // 检查时间段有效性
        if (serviceBeginTime == null || serviceEndTime == null) {
            return 0;
        }
        if (serviceBeginTime.after(serviceEndTime)) {
            return 0;
        }
        if(performanceConfirmTime == null) {
            performanceConfirmTime = new Date();
        }

        // 转换performanceConfirmTime到LocalDate并获取年份范围
        LocalDate perfDate = performanceConfirmTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = perfDate.getYear();
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);

        // 转换服务时间到LocalDates
        LocalDate begin = serviceBeginTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = serviceEndTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 计算实际覆盖的起止日期
        LocalDate actualStart = begin.isAfter(yearStart) ? begin : yearStart;
        LocalDate actualEnd = end.isBefore(yearEnd) ? end : yearEnd;

        // 检查是否有有效覆盖
        if (actualStart.isAfter(actualEnd)) {
            return 0;
        }

        // 生成覆盖的月份列表
        int startMonth = actualStart.getMonthValue();
        int endMonth = actualEnd.getMonthValue();

        return endMonth - startMonth + 1;
    }

}
