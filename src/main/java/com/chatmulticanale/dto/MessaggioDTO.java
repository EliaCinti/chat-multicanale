package com.chatmulticanale.dto;

import java.sql.Timestamp;

/**
 * Data Transfer Object per rappresentare un messaggio in canali di progetto o chat private.
 * Contiene i metadati del messaggio e opzionalmente un riferimento a un messaggio citato.
 */
public class MessaggioDTO {

    private int idMessaggio;
    private String contenuto;
    private Timestamp timestamp;
    private String usernameMittente;
    private Integer idMessaggioCitato;

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID del messaggio.
     *
     * @return identificativo univoco del messaggio
     */
    public int getIdMessaggio() {
        return idMessaggio;
    }

    /**
     * Imposta l'ID del messaggio.
     *
     * @param idMessaggio identificativo univoco da assegnare
     */
    public void setIdMessaggio(int idMessaggio) {
        this.idMessaggio = idMessaggio;
    }

    /**
     * Restituisce il contenuto del messaggio.
     *
     * @return testo del messaggio
     */
    public String getContenuto() {
        return contenuto;
    }

    /**
     * Imposta il contenuto del messaggio.
     *
     * @param contenuto testo del messaggio
     */
    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    /**
     * Restituisce il timestamp di invio del messaggio.
     *
     * @return data e ora di invio
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Imposta il timestamp di invio del messaggio.
     *
     * @param timestamp data e ora di invio
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Restituisce lo username del mittente.
     *
     * @return username dell'utente mittente
     */
    public String getUsernameMittente() {
        return usernameMittente;
    }

    /**
     * Imposta lo username del mittente.
     *
     * @param usernameMittente username dell'utente mittente
     */
    public void setUsernameMittente(String usernameMittente) {
        this.usernameMittente = usernameMittente;
    }

    /**
     * Restituisce l'ID del messaggio citato, se presente.
     *
     * @return identificativo del messaggio citato o {@code null} se non presente
     */
    public Integer getIdMessaggioCitato() {
        return idMessaggioCitato;
    }

    /**
     * Imposta l'ID del messaggio citato.
     *
     * @param idMessaggioCitato identificativo del messaggio citato o {@code null}
     */
    public void setIdMessaggioCitato(Integer idMessaggioCitato) {
        this.idMessaggioCitato = idMessaggioCitato;
    }
}