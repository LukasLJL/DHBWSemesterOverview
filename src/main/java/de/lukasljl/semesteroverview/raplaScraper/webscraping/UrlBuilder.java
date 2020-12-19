package de.lukasljl.semesteroverview.raplaScraper.webscraping;

import java.time.LocalDate;

public class UrlBuilder {
    private static final String baseUrl = "https://rapla.dhbw-stuttgart.de/rapla";

    public static String getUrl(String raplaKey, LocalDate dateToSearchFor){
        String base = baseUrl + "?key=" + raplaKey;

        base = appendParameter(base, "day", String.valueOf(dateToSearchFor.getDayOfMonth()));
        base = appendParameter(base, "month", String.valueOf(dateToSearchFor.getMonthValue()));
        base = appendParameter(base, "year", String.valueOf(dateToSearchFor.getYear()));

        return base;
    }

    private static String appendParameter(String previousURL, String key, String value){
        return previousURL + "&" + key + "=" + value;
    }
}
