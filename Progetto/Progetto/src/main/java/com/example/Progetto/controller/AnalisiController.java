package com.example.Progetto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.Progetto.AnalizzatoreFrase;
import java.io.IOException; 

@Controller
public class AnalisiController {
    
    // Questo metodo gestisce la richiesta GET per mostrare il form iniziale
    @GetMapping("/analizza")
    public String mostraForm() {
        return "analizza"; // Restituisce il nome del file analizza.html
    }
    
    @PostMapping("/analizza")
    public String analyzePhrase(@RequestParam("frase") String frase, Model model) {
        try {
            AnalizzatoreFrase analizzatore = new AnalizzatoreFrase();
            analizzatore.analizzaFrase(frase);
            
            // Nota: ho rimosso la parte del dizionario per mantenere il codice più aderente a quello che hai fornito.
            // Se usi la classe Dizionario, il codice sarebbe leggermente diverso.
            model.addAttribute("nomi", analizzatore.getIteratoreNomi());
            model.addAttribute("aggettivi", analizzatore.getIteratoreAggettivi());
            model.addAttribute("verbi", analizzatore.getIteratoreVerbi());
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Errore durante l'analisi della frase.");
        }
        return "analizza"; // Restituisce lo stesso file HTML per mostrare i risultati
    }
}