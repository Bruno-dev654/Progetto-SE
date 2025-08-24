package com.example.Progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Questa classe si occupa di generare frasi casuali.
 * Utilizza un dizionario di parole e un set di strutture sintattiche predefinite.
 */
@Component
public class Generate {

    private final Dictionary dictionary;
    private final Random random = new Random();

    // Lista delle strutture sintattiche disponibili, corrispondenti a quelle in genera.html
    private final List<String> sintatticStructure = Arrays.asList(
            "", // Indice 0, non utilizzato
            "Random", // Index 1, handled as a special case
"The [noun] [verb] the [noun].",
"A [noun] [verb] a [noun].",
"The [noun] [verb] with a [noun].",
"The [adjective] [noun] [verb].",
"The [noun] [verb] an [adjective] [noun].",
"The [noun] [verb] why is [adjective].",
"Why is the [noun] [verb] the [noun]?",
"The [noun] that [verb] the [noun] is [adjective].",
"The [noun] [verb] and the [noun] [verb].",
"The [noun] that is [adjective], [verb] with the [noun].",
"When the [noun] [verb], the [noun] becomes [adjective]."
    );

    @Autowired
    public Generate(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Genera una frase basata sull'ID della struttura sintattica fornita.
     * @param idStruttura L'ID della struttura scelta dall'utente. Se 1, ne sceglie una casuale.
     * @return La frase generata come stringa.
     */
    
    public String createSentence(int structureId, List<String> userNames, List<String> userAdjectives, List<String> userVerbs) {
        // Controlla se i dizionari hanno parole a sufficienza
        if (dictionary.getNames().isEmpty() || dictionary.getVerbs().isEmpty() || dictionary.getAdjectives().isEmpty()) {
            return "Errore: Il dizionario non contiene abbastanza parole (nomi, verbi, aggettivi) per generare una frase.";
        }

        String structure;
        if (structureId == 1) { // L'opzione "Casuale"
            int randomIndex = random.nextInt(sintatticStructure.size() - 2) + 2;
            structure = sintatticStructure.get(randomIndex);
        } else if (structureId > 1 && structureId < sintatticStructure.size()) {
            structure = sintatticStructure.get(structureId);
        } else {
            return "Errore: Struttura sintattica non valida.";  
        }

        // Crea copie modificabili delle liste di parole dell'utente per poterle "consumare"
        List<String> availableUserNames = new ArrayList<>(userNames != null ? userNames : Collections.emptyList());
        List<String> availableUserAdjectives = new ArrayList<>(userAdjectives != null ? userAdjectives : Collections.emptyList());
        List<String> availableUserVerbs = new ArrayList<>(userVerbs != null ? userVerbs : Collections.emptyList());

        String generatedSentence = structure;

        // Sostituisce i segnaposto [noun]
        while (generatedSentence.contains("[noun]")) {
            String word;
            if (!availableUserNames.isEmpty()) {
                word = availableUserNames.remove(0); // Usa la parola dell'utente e la rimuove dalla lista
            } else {
                word = getRandomName(); // Se non ci sono più parole dell'utente, ne prende una a caso
            }
            generatedSentence = generatedSentence.replaceFirst("\\[noun\\]", word);
        }

        // Sostituisce i segnaposto [verb]
        while (generatedSentence.contains("[verb]")) {
            String word;
            if (!availableUserVerbs.isEmpty()) {
                word = availableUserVerbs.remove(0);
            } else {
                word = getRandomVerb();
            }
            generatedSentence = generatedSentence.replaceFirst("\\[verb\\]", word);
        }

        // Sostituisce i segnaposto [adjective]
        while (generatedSentence.contains("[adjective]")) {
            String word;
            if (!availableUserAdjectives.isEmpty()) {
                word = availableUserAdjectives.remove(0);
            } else {
                word = getRandomAdjective();
            }
            generatedSentence = generatedSentence.replaceFirst("\\[adjective\\]", word);
        }

        // Rende maiuscola la prima lettera della frase
        if (!generatedSentence.isEmpty()) {
            generatedSentence = generatedSentence.substring(0, 1).toUpperCase() + generatedSentence.substring(1);
        }

        return generatedSentence;
    }

    // Metodi helper per ottenere parole casuali dalle liste del dizionario
    private String getRandomName() {
        List<String> names = dictionary.getNames();
        return names.get(random.nextInt(names.size()));
    }

    private String getRandomVerb() {
        List<String> verbs = dictionary.getVerbs();
        return verbs.get(random.nextInt(verbs.size()));
    }

    private String getRandomAdjective() {
        List<String> adjectives = dictionary.getAdjectives();
        return adjectives.get(random.nextInt(adjectives.size()));
    }
}
