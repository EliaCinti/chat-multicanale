package com.chatmulticanale.dto;

/**
 * Data Transfer Object per rappresentare i dati aggregati di un progetto e del suo responsabile.
 * Utilizzato nelle visualizzazioni che richiedono un JOIN tra Progetto e Utente,
 * evitando di modificare i model principali.
 */
public class ProgettoResponsabileDTO {
    private int idProgetto;
    private String nomeProgetto;
    private String nomeResponsabile;
    private String cognomeResponsabile;
    private int idResponsabile;

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID del progetto.
     *
     * @return identificativo del progetto
     */
    public int getIdProgetto() {
        return idProgetto;
    }

    /**
     * Imposta l'ID del progetto.
     *
     * @param idProgetto identificativo da assegnare
     */
    public void setIdProgetto(int idProgetto) {
        this.idProgetto = idProgetto;
    }

    /**
     * Restituisce il nome del progetto.
     *
     * @return nome del progetto
     */
    public String getNomeProgetto() {
        return nomeProgetto;
    }

    /**
     * Imposta il nome del progetto.
     *
     * @param nomeProgetto nome da assegnare al progetto
     */
    public void setNomeProgetto(String nomeProgetto) {
        this.nomeProgetto = nomeProgetto;
    }

    /**
     * Restituisce il nome del responsabile.
     *
     * @return nome proprio dell'utente responsabile
     */
    public String getNomeResponsabile() {
        return nomeResponsabile;
    }

    /**
     * Imposta il nome del responsabile.
     *
     * @param nomeResponsabile nome proprio da assegnare
     */
    public void setNomeResponsabile(String nomeResponsabile) {
        this.nomeResponsabile = nomeResponsabile;
    }

    /**
     * Restituisce il cognome del responsabile.
     *
     * @return cognome dell'utente responsabile
     */
    public String getCognomeResponsabile() {
        return cognomeResponsabile;
    }

    /**
     * Imposta il cognome del responsabile.
     *
     * @param cognomeResponsabile cognome da assegnare
     */
    public void setCognomeResponsabile(String cognomeResponsabile) {
        this.cognomeResponsabile = cognomeResponsabile;
    }

    /**
     * Restituisce l'ID dell'utente responsabile.
     *
     * @return identificativo del responsabile
     */
    public int getIdResponsabile() {
        return idResponsabile;
    }

    /**
     * Imposta l'ID dell'utente responsabile.
     *
     * @param idResponsabile identificativo da assegnare
     */
    public void setIdResponsabile(int idResponsabile) {
        this.idResponsabile = idResponsabile;
    }
}
