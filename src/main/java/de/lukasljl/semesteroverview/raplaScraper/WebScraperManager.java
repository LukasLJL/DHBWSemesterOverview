package de.lukasljl.semesteroverview.raplaScraper;

import de.lukasljl.semesteroverview.raplaScraper.models.LectureOverview;
import de.lukasljl.semesteroverview.raplaScraper.models.Semester;

import java.time.LocalDate;
import java.util.Map;

public class WebScraperManager {

    public void getSemesterInformationConsole(String raplaKey) {
        SemesterManager semesterManager = new SemesterManager();
        LocalDate semesterStart = semesterManager.getSemesterStartDate(raplaKey);
        LocalDate semesterEnd = semesterManager.getSemesterEndDate(raplaKey);

        Semester currentSemester = new Semester(
                semesterStart,
                semesterEnd,
                semesterManager.getLecturesFromRapla(raplaKey, semesterStart, semesterEnd)
        );

        System.out.println("SemesterStart: " + currentSemester.getStartDate());
        System.out.println("SemesterEnd: " + currentSemester.getEndDate());
        System.out.println("Semester-Lectures: ");
        for (Map.Entry<String, LectureOverview> test : currentSemester.getLectures().entrySet()) {
            System.out.println(" Lecture: " + test.getValue().getTitle() + " // Counter-Lessons: " + test.getValue().getLessons() + " // EntireTime: " + test.getValue().getEntireTime().toString()
                    + " // SpentTime: " + test.getValue().getSpentTime() + " // Spent-Lessons: " + test.getValue().getSpentLessons() + " // RestTime: " + test.getValue().getRestTime() + " // Finish: " + test.getValue().getPercentFinishHTML());
        }
    }

    public Semester getSemesterInformation(String raplaKey) {
        SemesterManager semesterManager = new SemesterManager();
        LocalDate semesterStart = semesterManager.getSemesterStartDate(raplaKey);
        LocalDate semesterEnd = semesterManager.getSemesterEndDate(raplaKey);

        return new Semester(semesterStart, semesterEnd, semesterManager.getLecturesFromRapla(raplaKey, semesterStart, semesterEnd));
    }
}
