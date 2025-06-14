package com.chatmulticanale.model;

public class Progetto {
    private int idProgetto;
    private String nomeProgetto;
    private String descrizioneProgetto;
    private int idUtenteResponsabile;

    // --- GETTER E SETTER ---

    public int getIdProgetto() {
        return idProgetto;
    }

    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }

    public String getNomeProgetto() {
        return nomeProgetto;
    }

    public void setNomeProgetto(String nomeProgetto) {
        this.nomeProgetto = nomeProgetto;
    }

    public String getDescrizioneProgetto() {
        return descrizioneProgetto;
    }

    public void setDescrizioneProgetto(String descrizioneProgetto) {
        this.descrizioneProgetto = descrizioneProgetto;
    }

    public int getIdUtenteResponsabile() {
        return idUtenteResponsabile;
    }

    public void setIdUtenteResponsabile(int idUtenteResponsabile) {
        this.idUtenteResponsabile = idUtenteResponsabile;
    }
}
