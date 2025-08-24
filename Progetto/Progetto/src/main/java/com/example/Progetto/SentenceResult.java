package com.example.Progetto;

/**
 * Un semplice oggetto DTO (Data Transfer Object) per contenere una frase generata
 * e il risultato della sua analisi di tossicità.
 */
public class SentenceResult{

    private final String sentence;
    private final String toxicityMessage;
    private final boolean isToxic;

    public SentenceResult(String sentence, String toxicityMessage) {
        this.sentence = sentence;
        this.toxicityMessage = toxicityMessage;
        // Consideriamo la frase tossica se il messaggio non è "Frase OK"
        this.isToxic = !toxicityMessage.equals("Sentence OK");
    }

    // Metodi getter necessari per Thymeleaf per accedere ai dati
    public String getSentence() {
        return sentence;
    }

    public String getToxicityMessage() {
        return toxicityMessage;
    }

    public boolean isToxic() {
        return isToxic;
    }
}