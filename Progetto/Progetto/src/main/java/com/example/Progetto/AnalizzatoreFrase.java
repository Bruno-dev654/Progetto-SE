package com.example.Progetto;
//import com.google.cloud.language.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Token;
import com.google.cloud.language.v1.PartOfSpeech.Tag;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class AnalizzatoreFrase {

    private final LanguageServiceClient languageClient; // 2. Aggiungi il client come campo della classe
    private List<String> nomiFrase;
    private List<String> aggettiviFrase;
    private List<String> verbiFrase;

    @Autowired // 3. Inietta il LanguageServiceClient creato dalla configurazione
    public AnalizzatoreFrase(LanguageServiceClient languageClient) {
        this.languageClient = languageClient;
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
    public void analizzaFrase(String frase) {

        // Inizializza un nuovo dizionario
        Dizionario dizionario = new Dizionario();

        nomiFrase.clear();
        aggettiviFrase.clear();
        verbiFrase.clear();

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

            // Scorre ogni token (parola) nella risposta
            for (Token token : response.getTokensList()) {
                Tag partOfSpeech = token.getPartOfSpeech().getTag();
                String parola = token.getText().getContent();
                
                // Assegnazione il token all'array corretto in base al suo tag
                if (partOfSpeech == Tag.NOUN) 
                {
                    //controllo nel dizionario se il nome è già presente
                    daAggiungere = true; // Resetto la variabile per ogni nuovo nome
                    for (int i =0; i< dizionario.nomi.size(); i++)
                    {
                        if (dizionario.nomi.get(i).equals(parola)) 
                        {
                            daAggiungere = false; // Se il nome è già nel dizionario, non aggiungerlo
                            break;
                        }
                    }
                    if (daAggiungere) //se il nome non è stato trovato nel dizionario
                    {
                        dizionario.aggiungiNome(parola);    // Aggiungi il nome al dizionario
                    }
                    nomiFrase.add(parola);
                } 
                else if (partOfSpeech == Tag.ADJ) 
                {
                    //controllo nel dizionario se l'aggettivo è già presente
                    daAggiungere = true; // Resetto la variabile per ogni nuovo nome
                    for (int i =0; i< dizionario.aggettivi.size(); i++)
                    {
                        if (dizionario.aggettivi.get(i).equals(parola)) 
                        {
                            daAggiungere = false; // Se l'aggettivo è già nel dizionario, non aggiungerlo
                            break;
                        }
                    }
                    if (daAggiungere) //se l'aggettivo non è stato trovato nel dizionario
                    {
                        dizionario.aggiungiAggettivo(parola);   // Aggiungi l'aggettivo al dizionario
                    }
                    aggettiviFrase.add(parola);
                } 
                else if (partOfSpeech == Tag.VERB) 
                {
                    //controllo nel dizionario se il verbo è già presente
                    daAggiungere = true; // Resetto la variabile per ogni nuovo nome
                    for (int i =0; i< dizionario.verbi.size(); i++)
                    {
                        if (dizionario.verbi.get(i).equals(parola)) 
                        {
                            daAggiungere = false; // Se il nome è già nel dizionario, non aggiungerlo
                            break;
                        }
                    }
                    if (daAggiungere) //se il nome non è stato trovato nel dizionario
                    {
                        dizionario.aggiungiVerbo(parola);   // Aggiungi il verbo al dizionario  
                    }
                    verbiFrase.add(parola);
                }
            }
    }
}

