package com.world.delacruzpaulino.coworking.dal;

import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by delacruzpaulino on 5/14/16.
 */
public class Schedule {

    int day;
    Date from;
    Date to;

    public Schedule() {
    }

    public Schedule(int day, Date from, Date to) {
        this.day = day;
        this.from = from;
        this.to = to;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String toString() {

        String from = new SimpleDateFormat("hh:mm a").format(this.from);
        String to = new SimpleDateFormat("hh:mm a").format(this.to);
        String days = this.day == 0 ? "(Lun-Vie)":"(Sab-Dom)";

        return from +" - "+ to +" "+ days;
    }
}

