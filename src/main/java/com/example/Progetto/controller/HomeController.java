package com.example.Progetto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showForm() {
        return "form";
    }
    @PostMapping("/analyze")
    public String analyzePhrase(@RequestParam("frase") String frase, Model model) {
        // Esempio: Analisi semplice (conteggio parole)
        int wordCount = frase.trim().split("\\s+").length;
        int length = frase.length();
        String risultato = "La frase contiene " + wordCount + " parole e " + length + " caratteri.";

        model.addAttribute("risultato", risultato);
        return "form";
    }
}

