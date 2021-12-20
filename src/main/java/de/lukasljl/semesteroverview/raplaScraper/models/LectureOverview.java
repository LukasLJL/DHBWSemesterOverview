package de.lukasljl.semesteroverview.raplaScraper.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class LectureOverview extends Lecture {
    private int lessons;
    private int spentLessons;
    private double percentFinish;
    private Duration entireTime;
    private Duration restTime;
    private Duration spentTime;
    //for html stuff
    private String entireTimeHTML;
    private String spentTimeHTML;
    private String percentFinishHTML;

    public LectureOverview(String title, String lecturer, LocalTime startTime, LocalTime endTime, LocalDate date) {
        super(title, lecturer, startTime, endTime, date);
    }

    public int getLessons() {
        return lessons;
    }

    public void setLessons(int lessons) {
        this.lessons = lessons;
    }

    public int getSpentLessons() {
        return spentLessons;
    }

    public void setSpentLessons(int spentLessons) {
        this.spentLessons = spentLessons;
    }

    public Duration getEntireTime() {
        return entireTime;
    }

    public void setEntireTime(Duration entireTime) {
        this.entireTime = entireTime;
    }

    public Duration getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(Duration spentTime) {
        this.spentTime = spentTime;
    }

    public Duration getRestTime() {
        return restTime;
    }

    public void setRestTime(Duration restTime) {
        this.restTime = restTime;
    }

    public double getPercentFinish() {
        return percentFinish;
    }

    public void setPercentFinish(int percentFinish) {
        this.percentFinish = percentFinish;
    }

    public String getPercentFinishHTML() {
        return percentFinishHTML;
    }

    public void setPercentFinishHTMLAutomatically(){
        this.percentFinishHTML = percentFinish + "%";
    }

    public String getEntireTimeHTML() {
        return entireTimeHTML;
    }

    public void setEntireTimeHTML(String entireTimeHTML) {
        this.entireTimeHTML = entireTimeHTML;
    }

    public String getSpentTimeHTML() {
        return spentTimeHTML;
    }

    public void setSpentTimeHTML(String spentTimeHTML) {
        this.spentTimeHTML = spentTimeHTML;
    }
}
