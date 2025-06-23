package com.chatmulticanale.dto;

import java.sql.Timestamp;

/**
 * Data Transfer Object per rappresentare una chat privata e i relativi metadati.
 * Utilizzato per trasportare informazioni di una chat privata tra livello DAO e livello di presentazione.
 */
public class ChatPrivataDTO {

    private int idChat;
    private Timestamp dataCreazione;
    private String altroPartecipanteUsername;

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'identificativo della chat.
     *
     * @return id della chat privata
     */
    public int getIdChat() {
        return idChat;
    }

    /**
     * Imposta l'identificativo della chat.
     *
     * @param idChat identificativo univoco della chat privata
     */
    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    /**
     * Restituisce il timestamp di creazione della chat.
     *
     * @return data e ora di creazione della chat privata
     */
    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Imposta il timestamp di creazione della chat.
     *
     * @param dataCreazione data e ora di creazione della chat privata
     */
    public void setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    /**
     * Restituisce lo username dell'altro partecipante nella chat.
     *
     * @return username dell'altro utente nella chat privata
     */
    public String getAltroPartecipanteUsername() {
        return altroPartecipanteUsername;
    }

    /**
     * Imposta lo username dell'altro partecipante.
     *
     * @param altroPartecipanteUsername username dell'altro utente nella chat privata
     */
    public void setAltroPartecipanteUsername(String altroPartecipanteUsername) {
        this.altroPartecipanteUsername = altroPartecipanteUsername;
    }
}