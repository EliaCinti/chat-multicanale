package com.chatmulticanale.model;

import java.time.LocalDateTime;

/**
 * Rappresenta un messaggio scambiato in un canale di progetto o in una chat privata.
 * Contiene il contenuto, metadati temporali e riferimenti al contesto di invio.
 */
public class Messaggio {

    /**
     * Identificativo univoco del messaggio.
     */
    private int idMessaggio;

    /**
     * Testo del messaggio inviato.
     */
    private String contenuto;

    /**
     * Data e ora di invio del messaggio.
     */
    private LocalDateTime timestamp;

    /**
     * Identificativo del canale di progetto di riferimento; 0 se non applicabile.
     */
    private int idCanaleRiferimento;

    /**
     * Identificativo della chat privata di riferimento; 0 se non applicabile.
     */
    private int idChatRiferimento;

    /**
     * Identificativo del messaggio citato; 0 se non ci sono citazioni.
     */
    private int idMessaggioCitato;

    /**
     * Identificativo dell'utente mittente del messaggio.
     */
    private int idUtenteMittente;

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
     * Restituisce il contenuto testuale del messaggio.
     *
     * @return testo del messaggio
     */
    public String getContenuto() {
        return contenuto;
    }

    /**
     * Imposta il contenuto del messaggio.
     *
     * @param contenuto testo da assegnare al messaggio
     */
    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    /**
     * Restituisce il timestamp di invio.
     *
     * @return data e ora di invio del messaggio
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Imposta il timestamp di invio.
     *
     * @param timestamp data e ora di invio da assegnare
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Restituisce l'ID del canale di riferimento.
     *
     * @return identificativo del canale, o 0 se non applicabile
     */
    public int getIdCanaleRiferimento() {
        return idCanaleRiferimento;
    }

    /**
     * Imposta l'ID del canale di riferimento.
     *
     * @param idCanaleRiferimento identificativo del canale di progetto
     */
    public void setIdCanaleRiferimento(int idCanaleRiferimento) {
        this.idCanaleRiferimento = idCanaleRiferimento;
    }

    /**
     * Restituisce l'ID della chat privata di riferimento.
     *
     * @return identificativo della chat privata, o 0 se non applicabile
     */
    public int getIdChatRiferimento() {
        return idChatRiferimento;
    }

    /**
     * Imposta l'ID della chat privata di riferimento.
     *
     * @param idChatRiferimento identificativo della chat privata
     */
    public void setIdChatRiferimento(int idChatRiferimento) {
        this.idChatRiferimento = idChatRiferimento;
    }

    /**
     * Restituisce l'ID del messaggio citato.
     *
     * @return identificativo del messaggio citato, o 0 se non presente
     */
    public int getIdMessaggioCitato() {
        return idMessaggioCitato;
    }

    /**
     * Imposta l'ID del messaggio citato.
     *
     * @param idMessaggioCitato identificativo del messaggio citato
     */
    public void setIdMessaggioCitato(int idMessaggioCitato) {
        this.idMessaggioCitato = idMessaggioCitato;
    }

    /**
     * Restituisce l'ID dell'utente mittente.
     *
     * @return identificativo dell'utente che ha inviato il messaggio
     */
    public int getIdUtenteMittente() {
        return idUtenteMittente;
    }

    /**
     * Imposta l'ID dell'utente mittente.
     *
     * @param idUtenteMittente identificativo dell'utente mittente
     */
    public void setIdUtenteMittente(int idUtenteMittente) {
        this.idUtenteMittente = idUtenteMittente;
    }
}
