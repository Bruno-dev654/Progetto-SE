package com.example.Progetto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProgettoSeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoSeApplication.class, args);
	}

	/**
	 * Questo pezzo di codice viene eseguito non appena l'applicazione è pronta.
	 * Chiediamo a Spring di darci il bean "dizionario".
	 * Se Spring non è riuscito a crearlo, l'applicazione si bloccherà con un errore chiaro.
	 * Se invece funziona, vedremo i messaggi di caricamento e un messaggio finale di successo.
	 */
	@Bean
	public CommandLineRunner testDizionario(Dizionario dizionario) {
		return args -> {
			System.out.println("**************************************************");
			System.out.println("TEST DI AVVIO: Controllo del bean Dizionario...");
			if (dizionario != null) {
				System.out.println("SUCCESS: Il bean Dizionario è stato caricato correttamente!");
				System.out.println("Contenuto: " + dizionario.getNomi().size() + " nomi, " + dizionario.getAggettivi().size() + " aggettivi, " + dizionario.getVerbi().size() + " verbi.");
			} else {
				System.err.println("ERRORE CRITICO: Il bean Dizionario è nullo!");
			}
			System.out.println("**************************************************");
		};
	}
}
