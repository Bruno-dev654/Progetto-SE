package com.example.Progetto.controller;

import com.example.Progetto.Dizionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    private Dizionario dizionario;

    // Mostra la home con le liste
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("nomi", dizionario.getNomi());
        model.addAttribute("aggettivi", dizionario.getAggettivi());
        model.addAttribute("verbi", dizionario.getVerbi());
        return "index"; // index.html (Thymeleaf)
    }

    // Aggiunge una parola al dizionario
    @PostMapping("/dizionario/aggiungi")
    public String aggiungiParola(@RequestParam String parola,
                                 @RequestParam String tipo) {
        switch (tipo) {
            case "nome" -> dizionario.aggiungiNome(parola);
            case "aggettivo" -> dizionario.aggiungiAggettivo(parola);
            case "verbo" -> dizionario.aggiungiVerbo(parola);
        }
        // dopo l’inserimento torno alla home e aggiorno le liste
        return "redirect:/";
    }
}
