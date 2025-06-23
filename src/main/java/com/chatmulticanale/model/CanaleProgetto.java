package com.chatmulticanale.model;

import java.time.LocalDateTime;

/**
 * Rappresenta un canale di comunicazione all'interno di un progetto.
 * Contiene informazioni sul canale, inclusi metadati di creazione e associazioni.
 */
public class CanaleProgetto {

    /**
     * Identificativo univoco del canale.
     */
    private int idCanale;

    /**
     * Nome descrittivo del canale.
     */
    private String nomeCanale;

    /**
     * Descrizione del canale, utile per contestualizzare l'uso.
     */
    private String descrizioneCanale;

    /**
     * Data e ora in cui il canale è stato creato.
     */
    private LocalDateTime dataCreazione;

    /**
     * Identificativo dell'utente che ha creato il canale.
     */
    private int idUtenteCreatore;

    /**
     * Identificativo del progetto a cui il canale appartiene.
     */
    private int idProgetto;

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID del canale.
     *
     * @return identificativo univoco del canale
     */
    public int getIdCanale() {
        return idCanale;
    }

    /**
     * Imposta l'ID del canale.
     *
     * @param idCanale identificativo univoco da assegnare
     */
    public void setIdCanale(int idCanale) {
        this.idCanale = idCanale;
    }

    /**
     * Restituisce il nome del canale.
     *
     * @return nome descrittivo del canale
     */
    public String getNomeCanale() {
        return nomeCanale;
    }

    /**
     * Imposta il nome del canale.
     *
     * @param nomeCanale nome descrittivo del canale da assegnare
     */
    public void setNomeCanale(String nomeCanale) {
        this.nomeCanale = nomeCanale;
    }

    /**
     * Restituisce la descrizione del canale.
     *
     * @return descrizione contestuale del canale
     */
    public String getDescrizioneCanale() {
        return descrizioneCanale;
    }

    /**
     * Imposta la descrizione del canale.
     *
     * @param descrizioneCanale testo descrittivo del canale
     */
    public void setDescrizioneCanale(String descrizioneCanale) {
        this.descrizioneCanale = descrizioneCanale;
    }

    /**
     * Restituisce la data e ora di creazione del canale.
     *
     * @return timestamp di creazione
     */
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Imposta la data e ora di creazione del canale.
     *
     * @param dataCreazione timestamp di creazione da assegnare
     */
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    /**
     * Restituisce l'ID dell'utente creatore del canale.
     *
     * @return identificativo dell'utente creatore
     */
    public int getIdUtenteCreatore() {
        return idUtenteCreatore;
    }

    /**
     * Imposta l'ID dell'utente creatore del canale.
     *
     * @param idUtenteCreatore identificativo dell'utente creatore da assegnare
     */
    public void setIdUtenteCreatore(int idUtenteCreatore) {
        this.idUtenteCreatore = idUtenteCreatore;
    }

    /**
     * Restituisce l'ID del progetto associato.
     *
     * @return identificativo del progetto di appartenenza
     */
    public int getIdProgetto() {
        return idProgetto;
    }

    /**
     * Imposta l'ID del progetto associato al canale.
     *
     * @param idProgetto identificativo del progetto da assegnare
     */
    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }
}

