package com.example.svampkartan.DataModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;

public class _LocalDateTime implements Serializable {

    public int year;
    public Month month;
    public int dayOfMonth;
    public int hour;
    public int minute;
    public int second;
    public int nanoOfSecond;


    public _LocalDateTime() { }

    public _LocalDateTime(LocalDateTime localDateTime) {
        year = localDateTime.getYear();
        month = localDateTime.getMonth();
        dayOfMonth = localDateTime.getDayOfMonth();
        hour = localDateTime.getHour();
        minute = localDateTime.getMinute();
        second = localDateTime.getSecond();
        nanoOfSecond = localDateTime.getNano();
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
    }

}
