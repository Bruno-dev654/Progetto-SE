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
import java.util.List;

public class AnalizzatoreFrase {

    private List<String> nomiFrase;
    private List<String> aggettiviFrase;
    private List<String> verbiFrase;

    public AnalizzatoreFrase() {
        this.nomiFrase = new ArrayList<>();
        this.aggettiviFrase = new ArrayList<>();
        this.verbiFrase = new ArrayList<>();
    }

    // GETTER PER THYMELEAF
    public List<String> getNomiFrase() {
        return nomiFrase;
    }

    public List<String> getAggettiviFrase() {
        return aggettiviFrase;
    }

    public List<String> getVerbiFrase() {
        return verbiFrase;
    }

   //Metodi per vedere la grandezza delle liste
    public int getSizeNomi() {
        return nomiFrase.size();
    }

    public int getSizeAggettivi() {
        return aggettiviFrase.size();
    }

    public int getSizeVerbi() {
        return verbiFrase.size();
    }

    //Metodi per vedere se sono vuote le liste
    //Fatto per controllare se funzionava il metodo per analizare la frase
    public boolean isEmptyNomi()
    {
      if(nomiFrase.isEmpty()) return true;
      else return false;
    }

   public boolean isEmptyAggettivi()
    {
      if(aggettiviFrase.isEmpty()) return true;
      else return false;
    }

   public boolean isEmptyVerbi()
    {
      if(verbiFrase.isEmpty()) return true;
      else return false;
    }
    
    //Metodo di analizzazione della frase 
    public void analizzaFrase(String frase) throws IOException {
        //Dizionario dizionario = new Dizionario();
        nomiFrase.clear();
        aggettiviFrase.clear();
        verbiFrase.clear();


    
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
                    //dizionario.aggiungiNome(parola);    // Aggiungi il nome al dizionario
                    nomiFrase.add(parola);
                } else if (partOfSpeech == Tag.ADJ) {
                    //dizionario.aggiungiAggettivo(parola);   // Aggiungi l'aggettivo al dizionario
                    aggettiviFrase.add(parola);
                } else if (partOfSpeech == Tag.VERB) {
                    //dizionario.aggiungiVerbo(parola);   // Aggiungi il verbo al dizionario
                    verbiFrase.add(parola);
                }
            }
        }
    }
}
