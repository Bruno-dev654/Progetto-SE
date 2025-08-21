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
import com.google.cloud.language.v1.DependencyEdge;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalizzatoreFrase {

    private final LanguageServiceClient languageClient; 
    private final Dizionario dizionario; 
    private List<String> nomiFrase;
    private List<String> aggettiviFrase;
    private List<String> verbiFrase;
    private List<String> alberoSintattico;


    
    // Lista aggiunta per memorizzare le relazioni sintattiche contestuali
    private List<String> relazioniSintattiche;

    @Autowired 
    public AnalizzatoreFrase(LanguageServiceClient languageClient, Dizionario dizionario) {
        this.languageClient = languageClient;
        this.dizionario = dizionario;
        this.nomiFrase = new ArrayList<>();
        this.aggettiviFrase = new ArrayList<>();
        this.verbiFrase = new ArrayList<>();
        this.alberoSintattico = new ArrayList<>();
        this.relazioniSintattiche = new ArrayList<>();
    }

    // GETTER PER THYMELEAF
    public List<String> getNomiFrase() { return nomiFrase; }

    public List<String> getAggettiviFrase() { return aggettiviFrase; }

    public List<String> getVerbiFrase() { return verbiFrase; }

    public List<String> getAlberoSintattico() { return alberoSintattico; }

    public List<String> getRelazioniSintattiche() { return relazioniSintattiche; }

   //Metodi per vedere la grandezza delle liste
    public int getSizeNomi() { return nomiFrase.size(); }

    public int getSizeAggettivi() { return aggettiviFrase.size(); }

    public int getSizeVerbi() { return verbiFrase.size(); }
    
    public int getSizeRelazioni() { return relazioniSintattiche.size(); }

    //Metodi per vedere se sono vuote le liste
    public boolean isEmptyNomi() { return nomiFrase.isEmpty(); }

    public boolean isEmptyAggettivi() { return aggettiviFrase.isEmpty(); }

    public boolean isEmptyVerbi() { return verbiFrase.isEmpty(); }

    public boolean isEmptyRelazioni() { return relazioniSintattiche.isEmpty(); }

    // Metodo di analizzazione della frase, ora con analisi contestuale
    public void analizzaFrase(String frase) {
        // Pulisce le liste all'inizio di ogni nuova analisi
        nomiFrase.clear();
        aggettiviFrase.clear();
        verbiFrase.clear();
        alberoSintattico.clear();
        relazioniSintattiche.clear();

        boolean daAggiungere = true;

        // Inizializzazione il client per l'API di Google Cloud Natural Language
            
            // Creazione un oggetto Document che contiene la frase
            Document doc = Document.newBuilder()
                .setContent(frase)
                .setType(Type.PLAIN_TEXT)
                .build();

        // Crea la richiesta per l'analisi sintattica
        AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
            .setDocument(doc)
            .build();
        
        // Invia la richiesta e ottiene la risposta
        AnalyzeSyntaxResponse response = languageClient.analyzeSyntax(request);

        // Scorre ogni token (parola) nella risposta
        for (int i = 0; i < response.getTokensList().size(); i++) {
            Token token = response.getTokensList().get(i);
            Tag partOfSpeech = token.getPartOfSpeech().getTag();
            String parola = token.getText().getContent();
            List<Token> tokens = response.getTokensList();
            
            // Analisi grammaticale (parte originale del codice)
            if (partOfSpeech == Tag.NOUN) {
                if (!dizionario.nomi.contains(parola)) {
                    dizionario.aggiungiNome(parola);
                }
                nomiFrase.add(parola);
            } else if (partOfSpeech == Tag.ADJ) {
                if (!dizionario.aggettivi.contains(parola)) {
                    dizionario.aggiungiAggettivo(parola);
                }
                aggettiviFrase.add(parola);
            } else if (partOfSpeech == Tag.VERB) {
                if (!dizionario.verbi.contains(parola)) {
                    dizionario.aggiungiVerbo(parola);
                }
                verbiFrase.add(parola);
            }
            
            // Analisi contestuale (la parte che hai richiesto)
            // L'oggetto DependencyEdge contiene le informazioni sulla relazione sintattica
            DependencyEdge depEdge = token.getDependencyEdge();

            // headTokenIndex è l'indice della parola da cui dipende il token corrente
            int headTokenIndex = depEdge.getHeadTokenIndex();

            // label descrive la relazione sintattica tra il token e la sua "testa"
            String relazione = depEdge.getLabel().toString();

            // Trova la parola "testa" (head) a cui il token corrente è legato
            String parolaHead = "ROOT"; // "ROOT" se è la radice della frase
            if (headTokenIndex != i) { // Evita il caso in cui un token dipende da se stesso
                 if (headTokenIndex >= 0 && headTokenIndex < response.getTokensList().size()) {
                    parolaHead = response.getTokensList().get(headTokenIndex).getText().getContent();
                 }
            }

            // Aggiungi una descrizione della relazione alla nuova lista
            String descrizioneRelazione = String.format("La parola '%s' ha la relazione '%s' con la parola '%s'.", 
                                                      parola, relazione, parolaHead);
            relazioniSintattiche.add(descrizioneRelazione);


            if (token.getDependencyEdge().getLabel() == Label.ROOT) {
                String relazioneAlberoSintattico = String.format("'%s' <-- [ROOT]", parola);
                alberoSintattico.add(relazioneAlberoSintattico);
                } else {
                // Per tutti gli altri token, troviamo il token a cui sono collegati (head)
                // e costruiamo una stringa che descrive la relazione.
                int headTokenIndexAS = token.getDependencyEdge().getHeadTokenIndex();
                Token headToken = tokens.get(headTokenIndexAS);
                String headTokenText = headToken.getText().getContent();
                String label = token.getDependencyEdge().getLabel().toString();

                String relazioneAlberoSintattico = String.format("'%s' ---[%s]--> '%s'", parola, label, headTokenText);
                alberoSintattico.add(relazioneAlberoSintattico);
            }
        }
    }
}