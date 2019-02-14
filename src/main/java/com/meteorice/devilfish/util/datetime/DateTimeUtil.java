package com.meteorice.devilfish.util.datetime;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class DateTimeUtil {

    private final static DateTimeFormatter formatter_month = DateTimeFormatter.ofPattern("MM");
    private final static DateTimeFormatter formatter_day = DateTimeFormatter.ofPattern("dd");
    private final static DateTimeFormatter formatter_date = DateTimeFormatter.ofPattern("YYYY-MM-dd");
    private final static DateTimeFormatter formatter_fulldate = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
    private final static DateTimeFormatter formatter_time = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static List<Integer> fullHoursList;

    static {
        fullHoursList = Stream.iterate(0, item -> item + 1).limit(24)
                .collect(Collectors.toList());
    }

    /**
     * 返回
     * @return
     */
    public static List<Integer> getFullHoursList() {
        return fullHoursList;
    }
    /**
     * 将timestamp转为LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime timestamToDatetime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 将LocalDataTime转为timestamp
     *
     * @param ldt
     * @return
     */
    public static Timestamp datatimeToTimestamp(LocalDateTime ldt) {
        long timestamp = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return new Timestamp(timestamp);
    }

    /**
     * 获取当年
     *
     * @return
     */
    public static String getYear() {
        LocalDateTime time = LocalDateTime.now();
        return String.valueOf(time.getYear());
    }

    /**
     * 获取当月
     *
     * @return
     */
    public static String getMonth() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter_month);
    }

    /**
     * 获取当日
     *
     * @return
     */
    public static String getDay() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter_day);
    }

    public static String getDate() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter_date);
    }

    public static String getFullDate() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter_fulldate);
    }

    public static String getTime() {
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter_time);
    }
}
