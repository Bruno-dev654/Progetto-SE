package com.example.Progetto.controller;

import com.example.Progetto.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    private Dictionary dictionary;

    // Mostra la home con le liste
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("nomi", dictionary.getNames());
        model.addAttribute("aggettivi", dictionary.getAdjectives());
        model.addAttribute("verbi", dictionary.getVerbs());
        return "index"; // index.html (Thymeleaf)
    }

    // Aggiunge una parola al dizionario
    @PostMapping("/dizionario/aggiungi")
    public String addWord(@RequestParam String word,
                                 @RequestParam String type) {
        switch (type) {
            case "nome" -> dictionary.addName(word);
            case "aggettivo" -> dictionary.addAdjective(word);
            case "verbo" -> dictionary.addVerb(word);
        }
        // dopo l’inserimento torno alla home e aggiorno le liste
        return "redirect:/";
    }
}
