package com.chatmulticanale.model;

import java.time.LocalDateTime;

/**
 * Rappresenta una chat privata tra due utenti all'interno del sistema.
 * Contiene metadati sulla chat, inclusi gli utenti coinvolti e il messaggio di origine.
 */
public class ChatPrivata {

    /**
     * Identificativo univoco della chat privata.
     */
    private int idChat;

    /**
     * Data e ora di creazione della chat.
     */
    private LocalDateTime dataCreazione;

    /**
     * Identificativo dell'utente che ha avviato la chat.
     */
    private int idUtenteCreatore;

    /**
     * Identificativo dell'altro utente partecipante alla chat.
     */
    private int idUtentePartecipante;

    /**
     * Identificativo del messaggio nel canale di origine da cui è nata la chat.
     */
    private int idMessaggioOrigineChat;

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID della chat privata.
     *
     * @return identificativo univoco della chat
     */
    public int getIdChat() {
        return idChat;
    }

    /**
     * Imposta l'ID della chat privata.
     *
     * @param idChat identificativo univoco da assegnare
     */
    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    /**
     * Restituisce la data e ora di creazione della chat.
     *
     * @return timestamp di creazione della chat
     */
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Imposta la data e ora di creazione della chat.
     *
     * @param dataCreazione timestamp di creazione da assegnare
     */
    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    /**
     * Restituisce l'ID dell'utente creatore della chat.
     *
     * @return identificativo dell'utente che ha avviato la chat
     */
    public int getIdUtenteCreatore() {
        return idUtenteCreatore;
    }

    /**
     * Imposta l'ID dell'utente creatore della chat.
     *
     * @param idUtenteCreatore identificativo dell'utente creatore da assegnare
     */
    public void setIdUtenteCreatore(int idUtenteCreatore) {
        this.idUtenteCreatore = idUtenteCreatore;
    }

    /**
     * Restituisce l'ID dell'altro partecipante alla chat.
     *
     * @return identificativo dell'altro utente partecipante
     */
    public int getIdUtentePartecipante() {
        return idUtentePartecipante;
    }

    /**
     * Imposta l'ID dell'altro partecipante alla chat.
     *
     * @param idUtentePartecipante identificativo dell'utente partecipante da assegnare
     */
    public void setIdUtentePartecipante(int idUtentePartecipante) {
        this.idUtentePartecipante = idUtentePartecipante;
    }

    /**
     * Restituisce l'ID del messaggio di origine della chat.
     *
     * @return identificativo del messaggio nel canale di origine
     */
    public int getIdMessaggioOrigineChat() {
        return idMessaggioOrigineChat;
    }

    /**
     * Imposta l'ID del messaggio di origine della chat.
     *
     * @param idMessaggioOrigineChat identificativo del messaggio di origine da assegnare
     */
    public void setIdMessaggioOrigineChat(int idMessaggioOrigineChat) {
        this.idMessaggioOrigineChat = idMessaggioOrigineChat;
    }
}

