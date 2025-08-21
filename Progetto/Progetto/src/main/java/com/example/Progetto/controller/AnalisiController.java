package com.example.Progetto.controller;


import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.Progetto.AnalizzatoreFrase;

import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class AnalisiController {
    
    // Questo metodo gestisce la richiesta GET per mostrare il form iniziale
    @GetMapping("/analizza")
    public String analizza() { return "analizza"; }

    @GetMapping("/index")
    public String index() { return "index"; }

    // Questo metodo gestirà la richiesta per la pagina "genera"
    @GetMapping("/genera")
    public String generaPage() { return "genera"; }
    
    // 1. Dichiara il servizio come dipendenza
    private final AnalizzatoreFrase analizzatore;

    // 2. Inietta il servizio tramite il costruttore
    @Autowired
    public AnalisiController(AnalizzatoreFrase analizzatore) {
        this.analizzatore = analizzatore;
    }



    @PostMapping("/analizza")
    public String analyzePhrase(@RequestParam("frase") String frase,@RequestParam(name = "albero_sintattico", required = false, defaultValue = "false") boolean visualizzaAlbero, Model model){
        
           try {
            // Se la frase è vuota, non facciamo l'analisi
            if (frase == null || frase.trim().isEmpty()) {
                model.addAttribute("error", "Per favore, inserisci una frase da analizzare.");
                return "analizza";
            }
            analizzatore.analizzaFrase(frase);

            // Aggiungi le liste di risultati al model per passarle a Thymeleaf.
            model.addAttribute("nomi", analizzatore.getNomiFrase());
            model.addAttribute("aggettivi", analizzatore.getAggettiviFrase());
            model.addAttribute("verbi", analizzatore.getVerbiFrase());


            model.addAttribute("sizeNomi", analizzatore.getSizeNomi());
            model.addAttribute("sizeAggettivi", analizzatore.getSizeAggettivi());
            model.addAttribute("sizeVerbi", analizzatore.getSizeVerbi());

            // Aggiunge la scelta dell'utente al modello, così la pagina sa se mostrare l'albero
            model.addAttribute("visualizzaAlbero", visualizzaAlbero);
            
            // Se l'utente ha richiesto l'albero, aggiungiamo i dati dell'albero al modello.
            if (visualizzaAlbero) {
                model.addAttribute("albero", analizzatore.getAlberoSintattico());
            }
            
            } catch (Exception e) { // Cambia IOException con Exception per catturare ogni tipo di errore
            e.printStackTrace(); // Stampa l'errore completo nel log
            model.addAttribute("error", "Errore durante l'analisi: " + e.getMessage());
            }
        
            // Nota: ho rimosso la parte del dizionario per mantenere il codice più aderente a quello che hai fornito.
            // Se usi la classe Dizionario, il codice sarebbe leggermente diverso.

        return "analizza"; // Restituisce lo stesso file HTML per mostrare i risultati
    }
}