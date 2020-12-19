package de.lukasljl.semesteroverview.raplaScraper;

import de.lukasljl.semesteroverview.raplaScraper.models.Lecture;
import de.lukasljl.semesteroverview.raplaScraper.models.LectureOverview;
import de.lukasljl.semesteroverview.raplaScraper.webscraping.Scraper;
import de.lukasljl.semesteroverview.raplaScraper.webscraping.UrlBuilder;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class SemesterManager {

    public SemesterManager() {

    }


    public LocalDate getSemesterStartDate(String raplaKey) {
        boolean foundSemesterStartDate = false;
        int emptyWeekCounter = 0;

        //getSemester startDate from Rapla
        LocalDate semesterStartDate = LocalDate.now();
        while (!foundSemesterStartDate) {
            Scraper scraper = new Scraper(UrlBuilder.getUrl(raplaKey, semesterStartDate));
            ArrayList<Lecture> lectureDays = scraper.getLectureDaysFromPage();
            if (lectureDays.isEmpty()) {
                emptyWeekCounter++;
            }
            if (emptyWeekCounter == 3) {
                foundSemesterStartDate = true;
                semesterStartDate = semesterStartDate.plusWeeks(3);
            } else {
                semesterStartDate = semesterStartDate.minusWeeks(1);
            }

            semesterStartDate = setDayToMonday(semesterStartDate);
        }
        return semesterStartDate;
    }

    public LocalDate getSemesterEndDate(String raplaKey) {
        boolean foundSemesterEndDate = false;
        //getSemesterEndDate
        LocalDate semesterEndDate = LocalDate.now();
        semesterEndDate = setDayToMonday(semesterEndDate);

        while (!foundSemesterEndDate) {
            Scraper scraper = new Scraper(UrlBuilder.getUrl(raplaKey, semesterEndDate));
            ArrayList<Lecture> lectureDays = scraper.getLectureDaysFromPage();

            if (!lectureDays.isEmpty()) {
                if (lectureDays.get(1).isKlausur()) {
                    foundSemesterEndDate = true;
                } else {
                    semesterEndDate = semesterEndDate.plusWeeks(1);
                }
            } else {
                semesterEndDate = semesterEndDate.plusWeeks(1);
            }
        }
        return semesterEndDate;
    }

    public HashMap<String, LectureOverview> getLecturesFromRapla(String raplaKey, LocalDate semesterStart, LocalDate semesterEnd) {
        HashMap<String, LectureOverview> allLectures = new HashMap<>();

        //getSemesterLectures
        while (semesterStart.isBefore(semesterEnd)) {
            Scraper scraper = new Scraper(UrlBuilder.getUrl(raplaKey, semesterStart));
            ArrayList<Lecture> lectureDays = scraper.getLectureDaysFromPage();

            for (Lecture lecture : lectureDays) {
                if (!allLectures.containsKey(lecture.getTitle())) {
                    LectureOverview lectureOverview = new LectureOverview(lecture.getTitle(), lecture.getLecturer(), lecture.getStartTime(), lecture.getEndTime(), lecture.getDate());
                    //Set special information
                    lectureOverview.setLessons(lectureOverview.getLessons() + 1);
                    lectureOverview.setEntireTime(Duration.between(lecture.getStartTime(), lecture.getEndTime()));

                    //Spend time & lessons
                    if (semesterStart.isBefore(LocalDate.now())) {
                        lectureOverview.setSpentTime(Duration.between(lecture.getStartTime(), lecture.getEndTime()));
                        lectureOverview.setSpentLessons(1);
                    } else {
                        lectureOverview.setSpentTime(Duration.ofMinutes(0));
                    }

                    lectureOverview.setRestTime(Duration.ofMinutes(0));

                    //add Object to map
                    allLectures.put(lecture.getTitle(), lectureOverview);
                } else {
                    //Update properties of existing lecture
                    LectureOverview tempLectureOverview = allLectures.get(lecture.getTitle());
                    tempLectureOverview.setLessons(tempLectureOverview.getLessons() + 1);
                    tempLectureOverview.setEntireTime(tempLectureOverview.getEntireTime().plus(Duration.between(lecture.getStartTime(), lecture.getEndTime())));

                    if (semesterStart.isBefore(LocalDate.now())) {
                        tempLectureOverview.setSpentLessons(tempLectureOverview.getSpentLessons() + 1);
                        tempLectureOverview.setSpentTime(tempLectureOverview.getSpentTime().plus(Duration.between(lecture.getStartTime(), lecture.getEndTime())));
                    }

                    tempLectureOverview.setRestTime(tempLectureOverview.getEntireTime().minus(tempLectureOverview.getSpentTime()));
                    allLectures.replace(lecture.getTitle(), tempLectureOverview);
                }

            }
            semesterStart = semesterStart.plusWeeks(1);
        }
        return allLectures;
    }

    private LocalDate setDayToMonday(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case TUESDAY:
                date = date.minusDays(1);
                break;
            case WEDNESDAY:
                date = date.minusDays(2);
                break;
            case THURSDAY:
                date = date.minusDays(3);
                break;
            case FRIDAY:
                date = date.minusDays(4);
                break;
            case SATURDAY:
                date = date.minusDays(5);
                break;
            case SUNDAY:
                date = date.minusDays(6);
            default:
                break;
        }
        return date;
    }

}
