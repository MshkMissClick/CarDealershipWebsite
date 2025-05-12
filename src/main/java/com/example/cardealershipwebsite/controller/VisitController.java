package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.service.VisitService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**Da.*/
@RestController
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;

    /**Da.*/
    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    /** Зарегистрировать посещение URL. */
    @PostMapping("/register")
    public String registerVisit(@RequestParam String url) {
        visitService.registerVisit(url);
        return "Visit registered for: " + url;
    }

    /** Получить количество посещений конкретного URL. */
    @GetMapping("/count")
    public int getVisitCount(@RequestParam String url) {
        return visitService.getVisitCount(url);
    }

    /** Получить все посещения. */
    @GetMapping("/all")
    public Map<String, Integer> getAllVisits() {
        return visitService.getAllVisitCounts();
    }
}