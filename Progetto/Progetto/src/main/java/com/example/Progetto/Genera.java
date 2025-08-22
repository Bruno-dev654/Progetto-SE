package com.example.Progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Questa classe si occupa di generare frasi casuali.
 * Utilizza un dizionario di parole e un set di strutture sintattiche predefinite.
 */
@Component
public class Genera {

    private final Dizionario dizionario;
    private final Random random = new Random();

    // Lista delle strutture sintattiche disponibili, corrispondenti a quelle in genera.html
    private final List<String> struttureSintattiche = Arrays.asList(
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
    public Genera(Dizionario dizionario) {
        this.dizionario = dizionario;
    }

    /**
     * Genera una frase basata sull'ID della struttura sintattica fornita.
     * @param idStruttura L'ID della struttura scelta dall'utente. Se 1, ne sceglie una casuale.
     * @return La frase generata come stringa.
     */
    
    public String creaFrase(int idStruttura) {
        // Controlla se i dizionari hanno parole a sufficienza
        if (dizionario.getNomi().isEmpty() || dizionario.getVerbi().isEmpty() || dizionario.getAggettivi().isEmpty()) {
            return "Errore: Il dizionario non contiene abbastanza parole (nomi, verbi, aggettivi) per generare una frase.";
        }

        String struttura;
        if (idStruttura == 1) { // L'opzione "Casuale"
            // Seleziona una struttura casuale dalla lista (escludendo le prime due opzioni non valide)
            int randomIndex = random.nextInt(struttureSintattiche.size() - 2) + 2;
            struttura = struttureSintattiche.get(randomIndex);
        } else if (idStruttura > 1 && idStruttura < struttureSintattiche.size()) {
            struttura = struttureSintattiche.get(idStruttura);
        } else {
            return "Errore: Struttura sintattica non valida.";
        }

        // Sostituisce i segnaposto con parole casuali
        String fraseGenerata = struttura;
        while (fraseGenerata.contains("[nome]")) {
            fraseGenerata = fraseGenerata.replaceFirst("\\[nome\\]", getRandomNome());
        }
        while (fraseGenerata.contains("[verbo]")) {
            fraseGenerata = fraseGenerata.replaceFirst("\\[verbo\\]", getRandomVerbo());
        }
        while (fraseGenerata.contains("[aggettivo]")) {
            fraseGenerata = fraseGenerata.replaceFirst("\\[aggettivo\\]", getRandomAggettivo());
        }

        // Rende maiuscola la prima lettera della frase
        if (!fraseGenerata.isEmpty()) {
            fraseGenerata = fraseGenerata.substring(0, 1).toUpperCase() + fraseGenerata.substring(1);
        }

        return fraseGenerata;
    }

    // Metodi helper per ottenere parole casuali dalle liste del dizionario
    private String getRandomNome() {
        List<String> nomi = dizionario.getNomi();
        return nomi.get(random.nextInt(nomi.size()));
    }

    private String getRandomVerbo() {
        List<String> verbi = dizionario.getVerbi();
        return verbi.get(random.nextInt(verbi.size()));
    }

    private String getRandomAggettivo() {
        List<String> aggettivi = dizionario.getAggettivi();
        return aggettivi.get(random.nextInt(aggettivi.size()));
    }
}
