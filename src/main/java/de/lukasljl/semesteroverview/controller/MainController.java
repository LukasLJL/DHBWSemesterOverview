package de.lukasljl.semesteroverview.controller;

import de.lukasljl.semesteroverview.raplaScraper.WebScraperManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dev")
public class MainController {

    @RequestMapping("/")
    public String startPage(){
        return "index";
    }

    @RequestMapping("/test")
    public String test(@RequestParam String raplaKey, Model model) {
        WebScraperManager webScraperManager = new WebScraperManager();
        /*
        Usage:
        http://localhost:8080/dev/test?raplaKey=txB1FOi5xd1wUJBWuX8lJrog1ZNPBrE7IVuW7j2dZDqY8f6xFPQs3yRuH0p5-aHv

        other key:
        "txB1FOi5xd1wUJBWuX8lJrog1ZNPBrE7IVuW7j2dZDr_O1hXpmPLKouUqFJE_-Xc"

        */
        model.addAttribute("semester", webScraperManager.getSemesterInformation(raplaKey));
        return "overview";
    }
}
