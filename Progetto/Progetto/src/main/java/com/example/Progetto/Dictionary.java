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


public class Dictionary {

    public List<String> names = new ArrayList<>();
    public List<String> adjectives = new ArrayList<>();
    public List<String> verbs = new ArrayList<>();

    public void init() {
        System.out.println("--- Start Loading Dictionary ---");
        names = loadingListFromFile("nomi.txt");
        adjectives = loadingListFromFile("aggettivi.txt");
        verbs = loadingListFromFile("verbi.txt");
        System.out.println("--- Loading Completed ---");
        System.out.println("Total words loaded : Names=" + names.size() + ", Adjectives=" + adjectives.size() + ", Verbs=" + verbs.size());
    }

    private List<String> loadingListFromFile(String fileName) {
        System.out.println("INFO: Tentativo di caricamento del file '" + fileName + "' dal classpath...");
        List<String> wordsList = new ArrayList<>();
        
        try {
            // Cerca il file nelle risorse usando il ClassLoader
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);

            // Controlla se il file è stato effettivamente trovato
            if (is == null) {
                System.err.println("ERRORE: Impossibile trovare il file '" + fileName + "' nel classpath! Assicurati che si trovi in 'src/main/resources'.");
                return wordsList; // Ritorna la lista vuota
            }

            // Usa try-with-resources per garantire che lo stream venga chiuso automaticamente
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                wordsList = reader.lines()
                        .map(String::trim) // Rimuove spazi bianchi all'inizio e alla fine
                        .filter(line -> !line.isEmpty()) // Ignora le righe vuote
                        .collect(Collectors.toList());
                System.out.println("SUCCESS: Caricate " + wordsList.size() + " parole da '" + fileName + "'.");
            }

        } catch (Exception e) {
            System.err.println("ERRORE CRITICO: Eccezione durante la lettura del file '" + fileName + "'.");
            e.printStackTrace();
        }
        
        return wordsList;
    }

    // Metodi per aggiungere parole e ottenere le liste (invariati)
    public void addName(String name) {
        if (name != null && !name.trim().isEmpty() && !names.contains(name)) {
            names.add(name);
        }
    }

    public void addAdjective(String adjective) {
        if (adjective != null && !adjective.trim().isEmpty() && !adjectives.contains(adjective)) {
            adjectives.add(adjective);
        }
    }

    public void addVerb(String verb) {
        if (verb != null && !verb.trim().isEmpty() && !verbs.contains(verb)) {
            verbs.add(verb);
        }
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getAdjectives() {
        return adjectives;
    }

    public List<String> getVerbs() {
        return verbs;
    }
}