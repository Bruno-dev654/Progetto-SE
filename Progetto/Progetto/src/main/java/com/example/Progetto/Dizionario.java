package com.example.Progetto;

import java.util.ArrayList;
import java.util.List;

public class Dizionario {
    
    public static List<String> nomi;
    public static List<String> aggettivi;
    public static List<String> verbi;

    public Dizionario() {
        this.nomi = new ArrayList<>();
        this.aggettivi = new ArrayList<>();
        this.verbi = new ArrayList<>();
    }

    public void aggiungiNome(String nome) {
        this.nomi.add(nome);
    }

    public void aggiungiAggettivo(String aggettivo) {
        this.aggettivi.add(aggettivo);
    }

    public void aggiungiVerbo(String verbo) {
        this.verbi.add(verbo);
    }    
}