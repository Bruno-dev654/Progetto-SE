package com.example.Progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Token;
import com.google.cloud.language.v1.PartOfSpeech.Tag;
import com.google.cloud.language.v1.DependencyEdge.Label;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class AnalizzatoreFrase {

    private final LanguageServiceClient languageClient; // 2. Aggiungi il client come campo della classe
    private final Dizionario dizionario; // inietto il dizionario
    private List<String> nomiFrase;
    private List<String> aggettiviFrase;
    private List<String> verbiFrase;
    private List<String> alberoSintattico;



    @Autowired // 3. Inietta il LanguageServiceClient creato dalla configurazione
    public AnalizzatoreFrase(LanguageServiceClient languageClient, Dizionario dizionario) {
        this.languageClient = languageClient;
        this.dizionario = dizionario;
        this.nomiFrase = new ArrayList<>();
        this.aggettiviFrase = new ArrayList<>();
        this.verbiFrase = new ArrayList<>();
        this.alberoSintattico = new ArrayList<>();
    }

    // GETTER PER THYMELEAF
    public List<String> getNomiFrase() { return nomiFrase; }

    public List<String> getAggettiviFrase() { return aggettiviFrase; }

    public List<String> getVerbiFrase() { return verbiFrase; }

    public List<String> getAlberoSintattico() { return alberoSintattico; }

   //Metodi per vedere la grandezza delle liste
    public int getSizeNomi() { return nomiFrase.size(); }

    public int getSizeAggettivi() { return aggettiviFrase.size(); }

    public int getSizeVerbi() { return verbiFrase.size(); }

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
    public void analizzaFrase(String frase) {

        nomiFrase.clear();
        aggettiviFrase.clear();
        verbiFrase.clear();
        alberoSintattico.clear();

        boolean daAggiungere = true;

        // Inizializzazione il client per l'API di Google Cloud Natural Language
            
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
            AnalyzeSyntaxResponse response = languageClient.analyzeSyntax(request); // Usa il client iniettato

             // Otteniamo la lista completa dei token per poter recuperare il testo del token "head"
        List<Token> tokens = response.getTokensList();

        // Scorre ogni token (parola) nella risposta
        for (Token token : tokens) {
            Tag partOfSpeech = token.getPartOfSpeech().getTag();
            String parola = token.getText().getContent();
            
            // Assegnazione del token alla lista corretta in base al suo tag (logica invariata)
            if (partOfSpeech == Tag.NOUN) {
                // ... (la logica per aggiungere ai nomi e al dizionario rimane la stessa)
                daAggiungere = !dizionario.nomi.contains(parola);
                if (daAggiungere) {
                    dizionario.aggiungiNome(parola);
                }
                nomiFrase.add(parola);
            } 
            else if (partOfSpeech == Tag.ADJ) {
                // ... (la logica per aggiungere agli aggettivi e al dizionario rimane la stessa)
                daAggiungere = !dizionario.aggettivi.contains(parola);
                if (daAggiungere) {
                    dizionario.aggiungiAggettivo(parola);
                }
                aggettiviFrase.add(parola);
            } 
            else if (partOfSpeech == Tag.VERB) {
                // ... (la logica per aggiungere ai verbi e al dizionario rimane la stessa)
                daAggiungere = !dizionario.verbi.contains(parola);
                if (daAggiungere) {
                    dizionario.aggiungiVerbo(parola);
                }
                verbiFrase.add(parola);
            }
        

            if (token.getDependencyEdge().getLabel() == Label.ROOT) {
                String relazione = String.format("'%s' <-- [ROOT]", parola);
                alberoSintattico.add(relazione);
                } else {
                // Per tutti gli altri token, troviamo il token a cui sono collegati (head)
                // e costruiamo una stringa che descrive la relazione.
                int headTokenIndex = token.getDependencyEdge().getHeadTokenIndex();
                Token headToken = tokens.get(headTokenIndex);
                String headTokenText = headToken.getText().getContent();
                String label = token.getDependencyEdge().getLabel().toString();

                String relazione = String.format("'%s' ---[%s]--> '%s'", parola, label, headTokenText);
                alberoSintattico.add(relazione);
                }
        }
    }
}

