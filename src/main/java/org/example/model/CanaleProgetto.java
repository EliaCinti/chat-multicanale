package org.example.model;

import java.time.LocalDateTime;

public class CanaleProgetto {
    private int idCanale;
    private String nomeCanale;
    private String descrizioneCanale;
    private LocalDateTime dataCreazione;
    private int idUtenteCreatore;
    private int idProgetto;

    public int getIdCanale() {
        return idCanale;
    }

    public void setIdCanale(int idCanale) {
        this.idCanale = idCanale;
    }

    public String getNomeCanale() {
        return nomeCanale;
    }

    public void setNomeCanale(String nomeCanale) {
        this.nomeCanale = nomeCanale;
    }

    public String getDescrizioneCanale() {
        return descrizioneCanale;
    }

    public void setDescrizioneCanale(String descrizioneCanale) {
        this.descrizioneCanale = descrizioneCanale;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public int getIdUtenteCreatore() {
        return idUtenteCreatore;
    }

    public void setIdUtenteCreatore(int idUtenteCreatore) {
        this.idUtenteCreatore = idUtenteCreatore;
    }

    public int getIdProgetto() {
        return idProgetto;
    }

    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }
}
