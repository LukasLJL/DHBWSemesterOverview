package de.lukasljl.semesteroverview.raplaScraper.models;

import java.time.LocalDate;
import java.util.HashMap;

public class Semester {
    private LocalDate startDate;
    private LocalDate endDate;
    private HashMap<String, LectureOverview> lectures;

    public Semester(LocalDate startDate, LocalDate endDate, HashMap<String, LectureOverview> lectures) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.lectures = lectures;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public HashMap<String, LectureOverview> getLectures() {
        return lectures;
    }

    public void setLectures(HashMap<String, LectureOverview> lectures) {
        this.lectures = lectures;
    }
}
