package cn.ericweb.timetable.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedList;

/**
 * Created by eric on 17-2-21.
 */
public class Classtable implements Serializable {
    private LinkedList<Subject> subjects;

    private LinkedList<Activity> activities;

    private Date sessionStartDate;

    private int numberOfClassPerDay;

    private Time classStartTime;
    private LinkedList<Integer> intervals;

    public void pushActivities(Activity newActivity) {
        this.activities.add(newActivity);
    }

    public LinkedList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(LinkedList<Subject> subjects) {
        this.subjects = subjects;
    }

    public LinkedList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(LinkedList<Activity> activities) {
        this.activities = activities;
    }

    public Date getSessionStartDate() {
        return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
        this.sessionStartDate = sessionStartDate;
    }

    public int getNumberOfClassPerDay() {
        return numberOfClassPerDay;
    }

    public void setNumberOfClassPerDay(int numberOfClassPerDay) {
        this.numberOfClassPerDay = numberOfClassPerDay;
    }

    public Time getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(Time classStartTime) {
        this.classStartTime = classStartTime;
    }

    public LinkedList<Integer> getIntervals() {
        return intervals;
    }

    public void setIntervals(LinkedList<Integer> intervals) {
        this.intervals = intervals;
    }
}
