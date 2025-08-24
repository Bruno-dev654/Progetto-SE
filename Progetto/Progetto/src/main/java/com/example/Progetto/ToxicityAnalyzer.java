package com.example.Progetto;

import com.google.cloud.language.v2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class ToxicityAnalyzer {

    private static final float TOXICITY_THRESHOLD = 0.7f;
    
    // Inietta il LanguageServiceClient gestito da Spring
    private final LanguageServiceClient languageServiceClient;

    @Autowired
    public ToxicityAnalyzer(@Qualifier("languageServiceClientV2") LanguageServiceClient languageServiceClient) {
        this.languageServiceClient = languageServiceClient;
    }

    public String analyzeToxicity(String text) {
        // NON usare più try-with-resources qui, perché Spring gestisce il ciclo di vita del client.
        try {
            Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
            ModerateTextRequest request = ModerateTextRequest.newBuilder().setDocument(doc).build();
            ModerateTextResponse response = languageServiceClient.moderateText(request);

            for (ClassificationCategory category : response.getModerationCategoriesList()) {
                if (category.getConfidence() > TOXICITY_THRESHOLD) {
                    return String.format("Warning: Potentially inappropriate sentence (Category: %s, Score: %.2f)",
                            category.getName(), category.getConfidence());
                }
            }
            return "Sentence OK";

        } catch (Exception e) { // Usa una Exception più generica
            System.err.println("Error calling the Natural Language API: " + e.getMessage());
            e.printStackTrace();
            return "Error: Unable to analyze sentence toxicity.";
        }
    }
}