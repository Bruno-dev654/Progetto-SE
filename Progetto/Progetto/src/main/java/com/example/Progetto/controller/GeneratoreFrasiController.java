package com.example.Progetto.controller;

import com.example.Progetto.Dizionario;
import com.example.Progetto.Genera;
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
public class GeneratoreFrasiController {

    private final Genera genera;
    private final Dizionario dizionario;

    @Autowired
    public GeneratoreFrasiController(Genera genera, Dizionario dizionario) {
        this.genera = genera;
        this.dizionario = dizionario;
    }

    /**
     * Gestisce la richiesta POST per generare frasi.
     * @param numFrasi Il numero di frasi da generare.
     * @param strutturaId L'ID della struttura sintattica scelta.
     * @param nuoviNomi Eventuali nuovi nomi aggiunti dall'utente.
     * @param nuoviAggettivi Eventuali nuovi aggettivi aggiunti dall'utente.
     * @param nuoviVerbi Eventuali nuovi verbi aggiunti dall'utente.
     * @param model Il modello a cui aggiungere i dati per la vista.
     * @return Il nome della vista per mostrare i risultati.
     */
    @PostMapping("/genera-frasi")
    public String generaFrasi(
            @RequestParam("num-frasi") int numFrasi,
            @RequestParam("struttura-sintattica") int strutturaId,
            @RequestParam(name = "nomi[]", required = false) String[] nuoviNomi,
            @RequestParam(name = "aggettivi[]", required = false) String[] nuoviAggettivi,
            @RequestParam(name = "verbi[]", required = false) String[] nuoviVerbi,
            Model model) {

        // Aggiunge le nuove parole al dizionario, se ce ne sono
        if (nuoviNomi != null) {
            for (String nome : nuoviNomi) {
                dizionario.aggiungiNome(nome);
            }
        }
        if (nuoviAggettivi != null) {
            for (String aggettivo : nuoviAggettivi) {
                dizionario.aggiungiAggettivo(aggettivo);
            }
        }
        if (nuoviVerbi != null) {
            for (String verbo : nuoviVerbi) {
                dizionario.aggiungiVerbo(verbo);
            }
        }

        // Genera le frasi
        List<String> frasiGenerate = new ArrayList<>();
        for (int i = 0; i < numFrasi; i++) {
            frasiGenerate.add(genera.creaFrase(strutturaId));
        }

        // Aggiunge le frasi generate al modello per la visualizzazione
        model.addAttribute("frasi", frasiGenerate);

        return "risultatoGenera"; // Nome del file HTML per visualizzare i risultati
    }
}
