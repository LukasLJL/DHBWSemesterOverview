package de.lukasljl.semesteroverview.controller;

import de.lukasljl.semesteroverview.raplaScraper.WebScraperManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dhbw")
public class MainController {

    @RequestMapping("/")
    public String startPage() {
        return "index";
    }

    @RequestMapping("/overview")
    public String dhbwOverviewPage(@RequestParam (required = false) String raplaKey, Model model) {
        if (raplaKey == null || raplaKey.equals("")) {
            return "index.html";
        } else {
            WebScraperManager webScraperManager = new WebScraperManager();

            model.addAttribute("semester", webScraperManager.getSemesterInformation(raplaKey));
            return "overview";
        }
    }
}
