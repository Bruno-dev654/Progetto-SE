package com.example.Progetto;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Token;
import com.google.cloud.language.v1.PartOfSpeech.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AnalizzatoreFrase {

    private List<String> nomi;
    private List<String> aggettivi;
    private List<String> verbi;

    public AnalizzatoreFrase() {
        this.nomi = new ArrayList<>();
        this.aggettivi = new ArrayList<>();
        this.verbi = new ArrayList<>();
    }

    // Metodi per ottenere gli iteratori
    public Iterator<String> getIteratoreNomi() {
        return nomi.iterator();
    }

    public Iterator<String> getIteratoreAggettivi() {
        return aggettivi.iterator();
    }

    public Iterator<String> getIteratoreVerbi() {
        return verbi.iterator();
    }

    
    public void analizzaFrase(String frase) throws IOException {
        //Pulizia di ogni array per ogni analizzazione
        nomi.clear();
        aggettivi.clear();
        verbi.clear();

        // Inizializzazione il client per l'API di Google Cloud Natural Language
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            
            // Creazione un oggetto Document che contiene la frase
            Document doc = Document.newBuilder()
                .setContent(frase)
                .setType(Type.PLAIN_TEXT)
                .build();

            // Creazione la richiesta per l'analisi sintattica
            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                .setDocument(doc)
                .build();
            
            // Invio la richiesta e ottieni la risposta
            AnalyzeSyntaxResponse response = language.analyzeSyntax(request);

            // Scorre ogni token (parola) nella risposta
            for (Token token : response.getTokensList()) {
                Tag partOfSpeech = token.getPartOfSpeech().getTag();
                String parola = token.getText().getContent();
                
                // Assegnazione il token all'array corretto in base al suo tag
                if (partOfSpeech == Tag.NOUN) {
                    nomi.add(parola);
                } else if (partOfSpeech == Tag.ADJ) {
                    aggettivi.add(parola);
                } else if (partOfSpeech == Tag.VERB) {
                    //si può aggiumgere il tempo verbale in questo punto
                    verbi.add(parola);
                }
            }
        }
    }
}
