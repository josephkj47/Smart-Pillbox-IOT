package com.rset.pills4u;

public class ScheduleData {
    String TimeSlot;
    String PillboxID;

    public ScheduleData(String timeSlot, String pillboxID) {
        TimeSlot = timeSlot;
        PillboxID = pillboxID;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getPillboxID() {
        return PillboxID;
    }

    public void setPillboxID(String pillboxID) {
        PillboxID = pillboxID;
    }
}
