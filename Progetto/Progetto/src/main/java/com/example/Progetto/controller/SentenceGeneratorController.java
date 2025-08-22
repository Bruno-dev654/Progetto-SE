package com.example.Progetto.controller;

import com.example.Progetto.Dictionary;
import com.example.Progetto.Generate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller per gestire la generazione di frasi.
 * Riceve le impostazioni dall'utente, invoca il servizio di generazione
 * e mostra i risultati.
 */
@Controller
public class SentenceGeneratorController {

    private final Generate generate;
    private final Dictionary dictionary;

    @Autowired
    public SentenceGeneratorController(Generate generate, Dictionary dictionary) {
        this.generate = generate;
        this.dictionary = dictionary;
    }

    /**
     * Gestisce la richiesta POST per generare frasi.
     * @param sentenceNumber Il numero di frasi da generare.
     * @param structureId L'ID della struttura sintattica scelta.
     * @param newNames Eventuali nuovi nomi aggiunti dall'utente.
     * @param newAdjectives Eventuali nuovi aggettivi aggiunti dall'utente.
     * @param newVerbs Eventuali nuovi verbi aggiunti dall'utente.
     * @param model Il modello a cui aggiungere i dati per la vista.
     * @return Il nome della vista per mostrare i risultati.
     */
    @PostMapping("/genera-frasi")
    public String generateSentence(
            @RequestParam("num-frasi") int sentenceNumber,
            @RequestParam("struttura-sintattica") int structureId,
            @RequestParam(name = "nomi[]", required = false) String[] newNames,
            @RequestParam(name = "aggettivi[]", required = false) String[] newAdjectives,
            @RequestParam(name = "verbi[]", required = false) String[] newVerbs,
            Model model) {

        // Aggiunge le nuove parole al dizionario, se ce ne sono
        if (newNames != null) {
            for (String name : newNames) {
                dictionary.addName(name);
            }
        }
        if (newAdjectives != null) {
            for (String adjective : newAdjectives) {
                dictionary.addAdjective(adjective);
            }
        }
        if (newVerbs != null) {
            for (String verb : newVerbs) {
                dictionary.addVerb(verb);
            }
        }

        // Genera le frasi
        List<String> generatedSentences = new ArrayList<>();
        for (int i = 0; i < sentenceNumber; i++) {
            generatedSentences.add(generate.createSentence(structureId));
        }

        // Aggiunge le frasi generate al modello per la visualizzazione
        model.addAttribute("frasi", generatedSentences);

        return "risultatoGenera"; // Nome del file HTML per visualizzare i risultati
    }
}
