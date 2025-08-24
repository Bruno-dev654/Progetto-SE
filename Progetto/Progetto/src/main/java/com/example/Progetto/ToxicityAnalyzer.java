package com.example.Progetto;

import com.google.cloud.language.v2.*;
import org.springframework.stereotype.Service;
import java.io.IOException;

/**
 * Servizio per analizzare la tossicità di un testo utilizzando
 * l'API Google Cloud Natural Language.
 */
@Service
public class ToxicityAnalyzer {

    // Soglia di confidenza oltre la quale una categoria viene considerata tossica.
    private static final float TOXICITY_THRESHOLD = 0.7f;

    /**
     * Analizza una frase e restituisce un messaggio che indica se è tossica o meno.
     * @param text Il testo da analizzare.
     * @return Una stringa con il risultato dell'analisi.
     */
    public String analyzeToxicity(String text) {
        // Usa try-with-resources per garantire che il client venga chiuso correttamente.
        try (LanguageServiceClient languageServiceClient = LanguageServiceClient.create()) {
            
            Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();

            // Chiama il nuovo metodo moderateText
            ModerateTextRequest request = ModerateTextRequest.newBuilder()
                .setDocument(doc)
                .build();
            
            ModerateTextResponse response = languageServiceClient.moderateText(request);

            // Scorre le categorie di moderazione restituite dall'API
            for (ClassificationCategory category : response.getModerationCategoriesList()) {
                // Se la confidenza per una categoria supera la nostra soglia, la segnaliamo.
                if (category.getConfidence() > TOXICITY_THRESHOLD) {
                    // Restituisce un messaggio di avviso con il nome della categoria rilevata
                    return String.format("Warning: Potentially inappropriate sentence (Category: %s, Score: %.2f)",
                            category.getName(), category.getConfidence());
                }
            }

            // Se nessuna categoria supera la soglia, la frase è considerata sicura.
            return "Sentence OK";

        } catch (IOException e) {
            System.err.println("Error calling the Natural Language API: " + e.getMessage());
            e.printStackTrace();
            // Restituisce un messaggio di errore se non è possibile contattare l'API
            return "Error: Unable to analyze sentence toxicity.";
        }
    }
}