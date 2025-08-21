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
            "Casuale", // Indice 1, gestito come caso speciale
            "Il [nome] [verbo] il [nome].",
            "Un [nome] [verbo] una [nome].",
            "Il [nome] [verbo] con un [nome].",
            "Il [aggettivo] [nome] [verbo].",
            "Il [nome] [verbo] un [aggettivo] [nome].",
            "Il [nome] [verbo] perché è [aggettivo].",
            "Perché il [nome] [verbo] il [nome]?",
            "Il [nome] che [verbo] il [nome] è [aggettivo].",
            "Il [nome] [verbo] e la [nome] [verbo].",
            "Il [nome], che è [aggettivo], [verbo] con il [nome].",
            "Quando il [nome] [verbo], il [nome] diventa [aggettivo]."
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
