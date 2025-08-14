package com.example.Progetto.controller;

import java.io.IOException;

//import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.Progetto.AnalizzatoreFrase;
 

@Controller
public class AnalisiController {
    
    // Questo metodo gestisce la richiesta GET per mostrare il form iniziale
    @GetMapping("/analizza")
    public String analizza() {
        return "analizza"; // Restituisce il nome del file analizza.html
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // Restituisce il nome del file analizza.html
    }

    // Questo metodo gestirà la richiesta per la pagina "genera"
    @GetMapping("/genera")
    public String generaPage() {
        return "genera"; // Restituisce il nome del file HTML senza estensione
    }
    
    @PostMapping("/analizza")
    public String analyzePhrase(@RequestParam("frase") String frase, Model model) throws IOException{
        AnalizzatoreFrase analizzatore = new AnalizzatoreFrase();
           try {
        
            analizzatore.analizzaFrase(frase);
            // ... il resto del tuo codice per aggiungere gli attributi
            } catch (Exception e) { // Cambia IOException con Exception per catturare ogni tipo di errore
            e.printStackTrace(); // Stampa l'errore completo nel log
            model.addAttribute("error", "Errore durante l'analisi: " + e.getMessage());
            }

            
            if (analizzatore.isEmptyNomi()) model.addAttribute("emptyNomi", "la lista nomi è vuota");
            else    model.addAttribute("emptyNomi", "la lista nomi è piena");

            if (analizzatore.isEmptyAggettivi()) model.addAttribute("emptyAggettivi", "la lista aggettivi è vuota");
            else    model.addAttribute("emptyAggettivi", "la lista aggettivi è piena");

            if (analizzatore.isEmptyVerbi()) model.addAttribute("emptyVerbi", "la lista verbi è vuota");
            else    model.addAttribute("emptyVerbi", "la lista verbi è piena");
            
            
            // Nota: ho rimosso la parte del dizionario per mantenere il codice più aderente a quello che hai fornito.
            // Se usi la classe Dizionario, il codice sarebbe leggermente diverso.

            /*
            model.addAttribute("nomi", analizzatore.getIteratoreNomi());
            model.addAttribute("aggettivi", analizzatore.getIteratoreAggettivi());
            model.addAttribute("verbi", analizzatore.getIteratoreVerbi());
            */


        return "analizza"; // Restituisce lo stesso file HTML per mostrare i risultati
    }
}