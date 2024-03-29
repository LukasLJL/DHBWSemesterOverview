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
        boolean backwards = true;
        boolean semesterIsRunning = false;
        int emptyWeekCounter = 0;
        int klausur = 0;

        //getSemester startDate from Rapla
        LocalDate semesterStartDate = LocalDate.now();
        semesterStartDate = setDayToMonday(semesterStartDate);
        while (!foundSemesterStartDate) {
            Scraper scraper = new Scraper(UrlBuilder.getUrl(raplaKey, semesterStartDate));
            ArrayList<Lecture> lectureDays = scraper.getLectureDaysFromPage();
            //Counter for Empty Weeeks
            if (lectureDays.isEmpty()) {
                emptyWeekCounter++;
            }
            //Check if there is a Klausur before the actual semester start and check if the semester started already
            if (!lectureDays.isEmpty()) {
                if (lectureDays.get(0).isKlausur()) {
                    klausur++;
                }
                semesterIsRunning = true;
            }
            //Reached emptyWeekCounter limit, switch to forward mode if there was nothing found in the past
            if (emptyWeekCounter == 3 && lectureDays.isEmpty() && backwards && !semesterIsRunning) {
                emptyWeekCounter = 0;
                semesterStartDate = LocalDate.now();
                semesterStartDate = setDayToMonday(semesterStartDate);
                backwards = false;
            }
            //Reached weekCounter limit. Now we found the actual semester start with the backward mode (happens when semester is already started)
            if (emptyWeekCounter == 3) {
                foundSemesterStartDate = true;
                if (backwards) {
                    semesterStartDate = semesterStartDate.plusWeeks(3+klausur);
                }
            } else if (!lectureDays.isEmpty() && emptyWeekCounter > 0) {
                emptyWeekCounter = 0;
            //Backward mode search for 3 empty week in a row
            } else if (backwards) {
                semesterStartDate = semesterStartDate.minusWeeks(1);
            //Forward mode search for empty week in the future
            } else if (!backwards) {
                if (lectureDays.isEmpty()){
                    semesterStartDate = semesterStartDate.plusWeeks(1);
                } else {
                    semesterStartDate = semesterStartDate.minusWeeks(emptyWeekCounter-klausur);
                    foundSemesterStartDate = true;
                }
            }
            semesterStartDate = setDayToMonday(semesterStartDate);
        }
        return semesterStartDate;
    }

    public LocalDate getSemesterEndDate(String raplaKey, LocalDate semesterStartDate) {
        boolean foundSemesterEndDate = false;
        //getSemesterEndDate
        LocalDate semesterEndDate = semesterStartDate;

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
                if (!lecture.isHoliday()) {
                    if (!allLectures.containsKey(lecture.getTitle())) {
                        LectureOverview lectureOverview = new LectureOverview(lecture.getTitle(), lecture.getLecturer(), lecture.getStartTime(), lecture.getEndTime(), lecture.getDate());
                        //Set special information
                        setSpecialInformation(lectureOverview, lecture, semesterStart);
                        //add Object to map
                        allLectures.put(lecture.getTitle(), lectureOverview);
                    } else {
                        //Update properties of existing lecture
                        LectureOverview tempLectureOverview = allLectures.get(lecture.getTitle());
                        setSpecialInformation(tempLectureOverview, lecture, semesterStart);
                        allLectures.replace(lecture.getTitle(), tempLectureOverview);
                    }
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

    private void setSpecialInformation(LectureOverview lectureOverview, Lecture lecture, LocalDate semesterStart) {
        lectureOverview.setLessons(lectureOverview.getLessons() + 1);

        //EntireTime is null on the first run
        if (lectureOverview.getEntireTime() == null) {
            lectureOverview.setEntireTime(Duration.between(lecture.getStartTime(), lecture.getEndTime()));
        } else {
            lectureOverview.setEntireTime(lectureOverview.getEntireTime().plus(Duration.between(lecture.getStartTime(), lecture.getEndTime())));
        }

        //Spent time & lessons
        if (semesterStart.isBefore(LocalDate.now())) {

            lectureOverview.setSpentLessons(lectureOverview.getSpentLessons() + 1);
            //SpentTime is null on the first run
            if (lectureOverview.getSpentTime() == null) {
                lectureOverview.setSpentTime(Duration.between(lecture.getStartTime(), lecture.getEndTime()));
            } else {
                lectureOverview.setSpentTime(lectureOverview.getSpentTime().plus(Duration.between(lecture.getStartTime(), lecture.getEndTime())));
            }
        }

        lectureOverview.setPercentFinish(lectureOverview.getSpentLessons() * 100 / lectureOverview.getLessons());
        lectureOverview.setPercentFinishHTMLAutomatically();

        if (lectureOverview.getSpentTime() == null) {
            lectureOverview.setRestTime(lectureOverview.getEntireTime());
            lectureOverview.setSpentTimeHTML("0S");
        } else {
            lectureOverview.setRestTime(lectureOverview.getEntireTime().minus(lectureOverview.getSpentTime()));
            lectureOverview.setSpentTimeHTML(lectureOverview.getSpentTime().toString().replace("PT", ""));
        }

        //For HTML stuff remove the PT from the duration
        lectureOverview.setEntireTimeHTML(lectureOverview.getEntireTime().toString().replace("PT", ""));

    }
}
