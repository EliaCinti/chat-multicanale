package com.chatmulticanale.dto;

/**
 * DTO (Data Transfer Object) per trasportare i dati combinati di un Progetto
 * e del suo Utente Responsabile. (simile alle bean).
 * Usato per visualizzazioni che richiedono un JOIN, senza "sporcare" i model principali.
 */
public class ProgettoResponsabileDTO {
    private int idProgetto;
    private String nomeProgetto;
    private String nomeResponsabile;
    private String cognomeResponsabile;
    private int idResponsabile;



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

    public String getNomeResponsabile() {
        return nomeResponsabile;
    }

    public void setNomeResponsabile(String nomeResponsabile) {
        this.nomeResponsabile = nomeResponsabile;
    }

    public String getCognomeResponsabile() {
        return cognomeResponsabile;
    }

    public void setCognomeResponsabile(String cognomeResponsabile) {
        this.cognomeResponsabile = cognomeResponsabile;
    }

    public int getIdResponsabile() {
        return idResponsabile;
    }

    public void setIdResponsabile(int idResponsabile) {
        this.idResponsabile = idResponsabile;
    }
}
