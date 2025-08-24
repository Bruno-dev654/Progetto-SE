package com.example.Progetto;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudConfig {

    private final String CREDENTIALS_FILE_PATH = "/se2025-468911-2c7deef522f5.json";

    /**
     * Carica le credenziali dal file JSON nella cartella /resources.
     * @return L'oggetto GoogleCredentials.
     * @throws IOException se il file non viene trovato.
     */
    private GoogleCredentials loadCredentials() throws IOException {
        InputStream credentialsStream = GoogleCloudConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (credentialsStream == null) {
            throw new IOException("File delle credenziali non trovato in resources: " + CREDENTIALS_FILE_PATH);
        }
        return GoogleCredentials.fromStream(credentialsStream)
                .createScoped("https://www.googleapis.com/auth/cloud-platform");
    }

    /**
     * Crea il bean per il client della versione v1 dell'API Language.
     * Usato dalla classe SentenceAnalyzer.
     * @return Un'istanza del client v1.
     * @throws IOException se le credenziali non possono essere caricate.
     */
    @Bean("languageServiceClientV1")
    public com.google.cloud.language.v1.LanguageServiceClient languageServiceClientV1() throws IOException {
        GoogleCredentials credentials = loadCredentials();
        com.google.cloud.language.v1.LanguageServiceSettings settings =
                com.google.cloud.language.v1.LanguageServiceSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();
        return com.google.cloud.language.v1.LanguageServiceClient.create(settings);
    }

    /**
     * Crea il bean per il client della versione v2 dell'API Language.
     * Usato dalla classe ToxicityAnalyzer.
     * @return Un'istanza del client v2.
     * @throws IOException se le credenziali non possono essere caricate.
     */
    @Bean("languageServiceClientV2")
    public com.google.cloud.language.v2.LanguageServiceClient languageServiceClientV2() throws IOException {
        GoogleCredentials credentials = loadCredentials();
        com.google.cloud.language.v2.LanguageServiceSettings settings =
                com.google.cloud.language.v2.LanguageServiceSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();
        return com.google.cloud.language.v2.LanguageServiceClient.create(settings);
    }
}
