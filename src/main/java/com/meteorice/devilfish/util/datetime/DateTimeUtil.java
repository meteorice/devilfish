package com.meteorice.devilfish.util.datetime;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 *
 */
public class DateTimeUtil {

    /**
     * 将timestamp转为LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public LocalDateTime timestamToDatetime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 将LocalDataTime转为timestamp
     *
     * @param ldt
     * @return
     */
    public long datatimeToTimestamp(LocalDateTime ldt) {
        long timestamp = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return timestamp;
    }

}
