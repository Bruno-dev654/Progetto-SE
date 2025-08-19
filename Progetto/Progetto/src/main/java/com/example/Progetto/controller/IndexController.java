package com.example.Progetto;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String home(Model model) {
        Dizionario dizionario = new Dizionario();

        // Passa le liste al modello
        model.addAttribute("nomi", dizionario.getNomi());
        model.addAttribute("aggettivi", dizionario.getAggettivi());
        model.addAttribute("verbi", dizionario.getVerbi());

        return "index"; // Restituisce il nome del template Thymeleaf (index.html)
    }
}
