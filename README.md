# Progetto-SE: Analisi e Generazione di Frasi
Benvenuto in Progetto-SE, un'applicazione Java basata su Spring Boot per l'analisi e la generazione di frasi. Il programma permette di analizzare la struttura sintattica di una frase, identificando nomi, aggettivi e verbi, e di generare nuove frasi a partire da modelli sintattici predefiniti o casuali.

Mediante l'utilizzo di un API (**Google Natural Language API**) il programma riconosce se la frase inserita ha una struttura sintattica sensata e costruisce, a richiesta dell'user, un syntactic-tree. La stessa API viene poi utilizzata per calcolare la tossicità delle frasi generate dal programma.

## Caratteristiche Principali
1. Analisi Morfologica: Analizza una frase fornita dall'utente e identifica le parti del discorso (nomi, aggettivi, verbi). 
2. Analisi Sintattica: Genera e visualizza su richiesta l'albero sintattico di una frase per mostrare la sua struttura sintattica.
3. Generazione di Frasi: Genera un numero n di frasi con le seguenti opzioni:
  a. Struttura Sintattica: L'utente può scegliere tra una lista di strutture predefinite o optare per una struttura casuale.
  b. Vocabolario: Le frasi generate combinano parole inserite dall'utente con parole prese da un vocabolario interno.
  c. Analisi di Tossicità: Valuta il livello di "tossicità" delle frasi generate per garantire la sicurezza del contenuto.

## Requisiti
Per avviare e far funzionare correttamente l'applicazione, assicurati di avere installato:
1. Java Development Kit (JDK) 17 o superiore
   `https://www.oracle.com/java/technologies/javase-downloads.html`
2. Maven 3.6.0 o superiore
3. Una IDE come IntelliJ IDEA o Eclipse (opzionale)

## Installazione
Segui questi passaggi per clonare e avviare il progetto:
1. Entrare su github nella repository Progetto-SE a questo link:
   git clone `https://github.com/Bruno-dev654/progetto-se.git`
2. alla home della repository di GitHub cliccare Code -> Download ZIP
   -cliaccando Download ZIP viene scaricato un file .zip contenente tutto il progetto
3. Aprire Esplora file e navigare alla cartella dove e' stato scaricato il file .zip
   -right-click sul file zip, "Estrai tutto" e selezionare la cartella in cui estrarre il contenuto del file
4. **IMPORTANTE**: E' necessario avere una chiave personale per le API GOOGLE NLP utilizzate nel programma, scaricare la chiave in formato json e         salvarla nella cartella base del progetto con nome se2025-468911-2c7deef522f5.json

//////////////NON SO SE SI FACCIAO COSÌ
5. Compila il progetto con Maven:
   mvn clean install

6. Avvia l'applicazione Spring Boot:
   mvn spring-boot:run
//////////////

L'applicazione sarà disponibile all'indirizzo `http://localhost:8080`.

## Utilizzo
Una volta avviata l'applicazione, potrai interagire con le sue funzionalità tramite un'interfaccia web semplice e intuitiva, accessibile tramite browser. 

## Architettura del Progetto
Il progetto segue il paradigma MVC (Model-View-Controller) tipico di Spring Boot, con i seguenti componenti principali:
1. Controller: Gestisce le richieste HTTP in ingresso e delega la logica di business ai servizi.
2. Service: Contiene la logica applicativa principale, inclusa l'analisi e la generazione delle frasi.
3. Model: Rappresenta la struttura dei dati.
4. Repository: Gestisce l'accesso ai dati (es. vocabolario interno), se presente.

## Indicazioni delle principali funzioni riutilizzate da librerie esistenti   
## Funzioni utilizzate dai file .java nella cartella **main**

| Libreria | Utilizzo |
|----------|----------|
| **analyzeSyntax**<br>`com.google.cloud.language.v1.`<br>`LanguageServiceClient.analyzeSyntax(Document document)` | Analyzes the syntax of the text and provides sentence boundaries and tokenization,<br>along with part of speech tags, dependency trees, and other properties. |
| **getDependencyEdge**<br>`com.google.cloud.language.v1.`<br>`Token.getDependencyEdge()` | Dependency tree parse for this token. |
| **moderateText**<br>`com.google.cloud.language.v1.`<br>`LanguageServiceClient.moderateText(ModerateTextRequest request)` | Moderates a document for harmful and sensitive categories. |

## Indicazione di principali API esterne utilizzate
1. Fondamentale per l'esecuzione corretta del programma è l'API Google Natural Language che viene usato nelle classi java sia per l'analisi delle frasi inserite dall'utente che nelle frasi generate dall'applicazione.
2. L'API verifica che le frasi inserite dall'utente siano sintatticamente corrette e ne stampa i syntactic-tree
3. Le frasi generate dal programma sono analizzate tramite Google Natural Langueage API per valutare il livello di tossicità
