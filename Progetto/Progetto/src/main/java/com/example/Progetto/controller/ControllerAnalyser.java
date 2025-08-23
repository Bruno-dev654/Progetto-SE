package com.example.Progetto.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Progetto.SentenceAnalyzer;

@Controller
public class ControllerAnalyser {

    private final SentenceAnalyzer analyzer;

    @Autowired
    public ControllerAnalyser(SentenceAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @GetMapping("/analizza")
    public String analyze() { 
        return "analizza"; 
    }

    @GetMapping("/index")
    public String index() { 
        return "index"; 
    }

    @GetMapping("/genera")
    public String generatePage() { 
        return "genera"; 
    }

    @PostMapping("/analizza")
    public String analyzePhrase(@RequestParam("frase") String sentence,@RequestParam(name = "albero_sintattico", required = false, defaultValue = "false") boolean visualizeTree, Model model){
        
           try {
            // Se la frase è vuota, non facciamo l'analisi
            if (sentence == null || sentence.trim().isEmpty()) {
                model.addAttribute("error", "Per favore, inserisci una frase da analizzare.");
                return "analizza";
            }

            analyzer.analyzeSentence(sentence);

            List<String> names = analyzer.getSentenceNames();
            List<String> adjectives = analyzer.getSentenceAdjectives();
            List<String> verbs = analyzer.getSentenceVerbs();
            List<String> relations = analyzer.getSintatticRelations();

            model.addAttribute("nomi", names);
            model.addAttribute("aggettivi", adjectives);
            model.addAttribute("verbi", verbs);
            model.addAttribute("relazioni", relations);

            model.addAttribute("sizeNomi", names.size());
            model.addAttribute("sizeAggettivi", adjectives.size());
            model.addAttribute("sizeVerbi", verbs.size());

            // Aggiunge la scelta dell'utente al modello, così la pagina sa se mostrare l'albero
            model.addAttribute("visualizzaAlbero", visualizeTree);
            
            // Se l'utente ha richiesto l'albero, aggiungiamo i dati dell'albero al modello.
            if (visualizeTree) {
                model.addAttribute("albero", analyzer.getSintatticTree());
            }
            
            // Ora valutiamo correttezza usando sia nomi che verbi
            boolean isCorrect = evaluateSintatticCorrectness(relations, names, verbs);
            model.addAttribute("isCorrect", isCorrect);
            model.addAttribute("feedbackCorrettezza", getFeedbackMessage(isCorrect));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Errore durante l'analisi: " + e.getMessage());
        }

        return "analizza";
    }

    private boolean evaluateSintatticCorrectness(List<String> sintatticRelations, List<String> names, List<String> verbs) {
        // Se manca almeno un nome o un verbo o non ci sono relazioni sintattiche, la frase è errata
        if (sintatticRelations == null || sintatticRelations.isEmpty()) return false;
        if (names == null || names.isEmpty()) return false;
        if (verbs == null || verbs.isEmpty()) return false;
    
        // Controllo ROOT: se non trova alcuna radice, frase errata
        boolean hasRoot = sintatticRelations.stream().anyMatch(rel -> rel.toUpperCase().contains("ROOT"));
    
        return hasRoot;
    }

    private String getFeedbackMessage(boolean isCorrect) {
        if (isCorrect) {
            return "The sentence seems syntactically correct!";
        } else {
            return "The sentence you entered may be syntactically incorrect. It may be missing a verb, a subject, or the elements are not well connected.";
        }
    }
}

