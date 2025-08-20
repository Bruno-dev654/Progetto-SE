package com.example.Progetto.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Progetto.AnalizzatoreFrase;

@Controller
public class AnalisiController {

    private final AnalizzatoreFrase analizzatore;

    @Autowired
    public AnalisiController(AnalizzatoreFrase analizzatore) {
        this.analizzatore = analizzatore;
    }

    @GetMapping("/analizza")
    public String analizza() { 
        return "analizza"; 
    }

    @GetMapping("/index")
    public String index() { 
        return "index"; 
    }

    @GetMapping("/genera")
    public String generaPage() { 
        return "genera"; 
    }

    @PostMapping("/analizza")
    public String analyzePhrase(@RequestParam("frase") String frase, 
                                @RequestParam(name = "albero_sintattico", required = false) boolean visualizzaAlbero, 
                                Model model) throws IOException {

        try {
            if (frase == null || frase.trim().isEmpty()) {
                model.addAttribute("error", "Per favore, inserisci una frase da analizzare.");
                return "analizza";
            }

            analizzatore.analizzaFrase(frase);

            List<String> nomi = analizzatore.getNomiFrase();
            List<String> aggettivi = analizzatore.getAggettiviFrase();
            List<String> verbi = analizzatore.getVerbiFrase();
            List<String> relazioni = analizzatore.getRelazioniSintattiche();

            model.addAttribute("nomi", nomi);
            model.addAttribute("aggettivi", aggettivi);
            model.addAttribute("verbi", verbi);
            model.addAttribute("relazioni", relazioni);

            model.addAttribute("sizeNomi", nomi.size());
            model.addAttribute("sizeAggettivi", aggettivi.size());
            model.addAttribute("sizeVerbi", verbi.size());

            // Ora valutiamo correttezza usando sia nomi che verbi
            boolean isCorrect = valutaCorrettezzaSintattica(relazioni, nomi, verbi);
            model.addAttribute("isCorrect", isCorrect);
            model.addAttribute("feedbackCorrettezza", getFeedbackMessage(isCorrect));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Errore durante l'analisi: " + e.getMessage());
        }

        return "analizza";
    }

    private boolean valutaCorrettezzaSintattica(List<String> relazioniSintattiche, List<String> nomi, List<String> verbi) {
        // Se manca almeno un nome o un verbo o non ci sono relazioni sintattiche, la frase è errata
        if (relazioniSintattiche == null || relazioniSintattiche.isEmpty()) return false;
        if (nomi == null || nomi.isEmpty()) return false;
        if (verbi == null || verbi.isEmpty()) return false;
    
        // Controllo ROOT: se non trova alcuna radice, frase errata
        boolean hasRoot = relazioniSintattiche.stream().anyMatch(rel -> rel.toUpperCase().contains("ROOT"));
    
        return hasRoot;
    }

    private String getFeedbackMessage(boolean isCorrect) {
        if (isCorrect) {
            return "La frase sembra sintatticamente corretta!";
        } else {
            return "La frase inserita potrebbe essere sintatticamente errata. Potrebbe mancare un verbo, un soggetto, o gli elementi non sono ben collegati.";
        }
    }
}
