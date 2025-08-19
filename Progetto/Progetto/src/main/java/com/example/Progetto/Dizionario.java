package com.example.Progetto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
@Component 
public class Dizionario {
    
    public static List<String> nomi=  new ArrayList<>();
    public static List<String> aggettivi = new ArrayList<>();
    public static List<String> verbi = new ArrayList<>();

    public void aggiungiNome(String nome) {
         if (!nomi.contains(nome)) {
            nomi.add(nome);
        }
    }

    public void aggiungiAggettivo(String aggettivo) {
        if (!aggettivi.contains(aggettivo)) {
            aggettivi.add(aggettivo);
        }
    }

    public void aggiungiVerbo(String verbo) {
        if (!verbi.contains(verbo)) {
            verbi.add(verbo);
        }
    }    

    public List<String> getNomi() {
        return nomi;
    }

    public List<String> getAggettivi() {
        return aggettivi;
    }

    public List<String> getVerbi() {
        return verbi;
    }
}
