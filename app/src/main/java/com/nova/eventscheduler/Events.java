package com.nova.eventscheduler;
import java.util.Calendar;

public class Events {
    private String eventName;
    private String eventDesc;
    private Calendar calendarV;
    private boolean alarmOn;

    public Events() {
        this.eventName = new String();
        this.eventDesc = new String();
        this.calendarV = Calendar.getInstance();
        this.alarmOn = false;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public Calendar getCalendarV() {
        return calendarV;
    }

    public void setCalendarV(Calendar calendarV) {
        this.calendarV = calendarV;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }
}
