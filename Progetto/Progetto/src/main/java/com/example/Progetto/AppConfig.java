package com.example.Progetto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Questa è una classe di configurazione esplicita per Spring.
 * Dice a Spring quali oggetti (bean) deve creare e gestire.
 */
@Configuration
public class AppConfig {

    /**
     * Questo metodo dice a Spring: "Quando qualcuno chiede un Dizionario,
     * esegui questo metodo, crea un nuovo oggetto Dizionario e restituiscilo".
     * L'oggetto creato verrà gestito come singleton.
     * @return L'unica istanza del Dizionario per l'applicazione.
     */
    @Bean
   public Dictionary dizionario() {
        // 1. Creiamo una nuova istanza del dizionario
        Dictionary nuovoDizionario = new Dictionary();
        
        // 2. Chiamiamo manualmente il suo metodo di inizializzazione per caricare i file
        nuovoDizionario.init();
        
        // 3. Restituiamo l'oggetto inizializzato a Spring
        return nuovoDizionario;
    }
}
