package com.example.Progetto.controller;

import com.example.Progetto.Dictionary;
import com.example.Progetto.Generate;
import com.example.Progetto.SentenceResult;
import com.example.Progetto.ToxicityAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private final ToxicityAnalyzer toxicityAnalyzer;

    @Autowired
    public SentenceGeneratorController(Generate generate, Dictionary dictionary, ToxicityAnalyzer toxicityAnalyzer) {
        this.generate = generate;
        this.dictionary = dictionary;
        this.toxicityAnalyzer = toxicityAnalyzer;
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

        // Converte gli array di nuove parole in Liste per passarle al generatore
        List<String> userNamesList = (newNames != null) ? Arrays.asList(newNames) : Collections.emptyList();
        List<String> userAdjectivesList = (newAdjectives != null) ? Arrays.asList(newAdjectives) : Collections.emptyList();
        List<String> userVerbsList = (newVerbs != null) ? Arrays.asList(newVerbs) : Collections.emptyList();


        // Lista per contenere i risultati completi (frase + analisi)
        List<SentenceResult> results = new ArrayList<>();

        for (int i = 0; i < sentenceNumber; i++) {
            // 1. Genera la frase
            String sentence = generate.createSentence(structureId, userNamesList, userAdjectivesList, userVerbsList);
            
            // 2. Analizza la tossicità della frase generata
            String toxicityMessage = toxicityAnalyzer.analyzeToxicity(sentence);
            
            // 3. Crea un oggetto risultato e lo aggiunge alla lista
            results.add(new SentenceResult(sentence, toxicityMessage));
        }

        // Aggiunge la lista di risultati al modello
        model.addAttribute("risultati", results);

        return "risultatoGenera"; // Nome del file HTML per visualizzare i risultati
    }
}
