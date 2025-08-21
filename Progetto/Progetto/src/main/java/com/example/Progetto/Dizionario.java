package com.example.Progetto;

//import org.springframework.stereotype.Component;
//import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Dizionario {

    public List<String> nomi = new ArrayList<>();
    public List<String> aggettivi = new ArrayList<>();
    public List<String> verbi = new ArrayList<>();

    public void init() {
        System.out.println("--- Inizio Caricamento Dizionari ---");
        nomi = caricaListaDaFile("nomi.txt");
        aggettivi = caricaListaDaFile("aggettivi.txt");
        verbi = caricaListaDaFile("verbi.txt");
        System.out.println("--- Caricamento Completato ---");
        System.out.println("Totale parole caricate: Nomi=" + nomi.size() + ", Aggettivi=" + aggettivi.size() + ", Verbi=" + verbi.size());
    }

    private List<String> caricaListaDaFile(String nomeFile) {
        System.out.println("INFO: Tentativo di caricamento del file '" + nomeFile + "' dal classpath...");
        List<String> listaParole = new ArrayList<>();
        
        try {
            // Cerca il file nelle risorse usando il ClassLoader
            InputStream is = getClass().getClassLoader().getResourceAsStream(nomeFile);

            // Controlla se il file è stato effettivamente trovato
            if (is == null) {
                System.err.println("ERRORE: Impossibile trovare il file '" + nomeFile + "' nel classpath! Assicurati che si trovi in 'src/main/resources'.");
                return listaParole; // Ritorna la lista vuota
            }

            // Usa try-with-resources per garantire che lo stream venga chiuso automaticamente
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                listaParole = reader.lines()
                        .map(String::trim) // Rimuove spazi bianchi all'inizio e alla fine
                        .filter(line -> !line.isEmpty()) // Ignora le righe vuote
                        .collect(Collectors.toList());
                System.out.println("SUCCESS: Caricate " + listaParole.size() + " parole da '" + nomeFile + "'.");
            }

        } catch (Exception e) {
            System.err.println("ERRORE CRITICO: Eccezione durante la lettura del file '" + nomeFile + "'.");
            e.printStackTrace();
        }
        
        return listaParole;
    }

    // Metodi per aggiungere parole e ottenere le liste (invariati)
    public void aggiungiNome(String nome) {
        if (nome != null && !nome.trim().isEmpty() && !nomi.contains(nome)) {
            nomi.add(nome);
        }
    }

    public void aggiungiAggettivo(String aggettivo) {
        if (aggettivo != null && !aggettivo.trim().isEmpty() && !aggettivi.contains(aggettivo)) {
            aggettivi.add(aggettivo);
        }
    }

    public void aggiungiVerbo(String verbo) {
        if (verbo != null && !verbo.trim().isEmpty() && !verbi.contains(verbo)) {
            verbi.add(verbo);
        }
    }

    public List<String> getNomi() {
        return nomi;
    }

    public List<String> getAggettivi() {
        return aggettivi;
    }

    public List<String> getVerbi() {
        return verbi;
    }
}