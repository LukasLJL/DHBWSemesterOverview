package de.lukasljl.semesteroverview.raplaScraper.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Lecture {
    private final String title;
    private final String lecturer;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final LocalDate date;
    private boolean isKlausur = false;
    private boolean isHoliday = false;

    public Lecture(String title, String lecturer, LocalTime startTime, LocalTime endTime, LocalDate date) {
        this.title = title;
        this.lecturer = lecturer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public Lecture(String title, String lecturer, LocalTime startTime, LocalTime endTime, LocalDate date, boolean isKlausur, boolean isHoliday) {
        this(title, lecturer, startTime, endTime, date);
        this.isKlausur = isKlausur;
        this.isHoliday = isHoliday;
    }

    public String getTitle() {
        return title;
    }

    public String getLecturer() {
        return lecturer;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isKlausur() {
        return isKlausur;
    }

    public boolean isHoliday(){
        return isHoliday;
    }

    @Override
    public int hashCode() {
        String uniqueIdentifier = title + lecturer + startTime.toString() + endTime.toString() + date.toString();
        return uniqueIdentifier.hashCode();
    }
}
