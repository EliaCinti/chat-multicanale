package com.chatmulticanale.model;

/**
 * Entità che rappresenta un progetto gestito all'interno del sistema.
 * Contiene informazioni essenziali come nome, descrizione e responsabile.
 */
public class Progetto {

    /**
     * Identificativo univoco del progetto.
     */
    private int idProgetto;

    /**
     * Nome descrittivo del progetto.
     */
    private String nomeProgetto;

    /**
     * Descrizione dettagliata del progetto.
     */
    private String descrizioneProgetto;

    /**
     * Identificativo dell'utente responsabile del progetto.
     */
    private int idUtenteResponsabile;

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID del progetto.
     *
     * @return identificativo univoco del progetto
     */
    public int getIdProgetto() {
        return idProgetto;
    }

    /**
     * Imposta l'ID del progetto.
     *
     * @param idProgetto identificativo univoco da assegnare
     */
    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }

    /**
     * Restituisce il nome del progetto.
     *
     * @return nome descrittivo del progetto
     */
    public String getNomeProgetto() {
        return nomeProgetto;
    }

    /**
     * Imposta il nome del progetto.
     *
     * @param nomeProgetto nome descrittivo da assegnare
     */
    public void setNomeProgetto(String nomeProgetto) {
        this.nomeProgetto = nomeProgetto;
    }

    /**
     * Restituisce la descrizione del progetto.
     *
     * @return descrizione dettagliata del progetto
     */
    public String getDescrizioneProgetto() {
        return descrizioneProgetto;
    }

    /**
     * Imposta la descrizione del progetto.
     *
     * @param descrizioneProgetto testo descrittivo da assegnare
     */
    public void setDescrizioneProgetto(String descrizioneProgetto) {
        this.descrizioneProgetto = descrizioneProgetto;
    }

    /**
     * Restituisce l'ID dell'utente responsabile.
     *
     * @return identificativo dell'utente responsabile del progetto
     */
    public int getIdUtenteResponsabile() {
        return idUtenteResponsabile;
    }

    /**
     * Imposta l'ID dell'utente responsabile del progetto.
     *
     * @param idUtenteResponsabile identificativo dell'utente responsabile da assegnare
     */
    public void setIdUtenteResponsabile(int idUtenteResponsabile) {
        this.idUtenteResponsabile = idUtenteResponsabile;
    }
}
