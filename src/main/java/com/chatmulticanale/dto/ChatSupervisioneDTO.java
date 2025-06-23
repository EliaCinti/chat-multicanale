package com.chatmulticanale.dto;

import java.sql.Timestamp;

/**
 * Data Transfer Object utilizzato per la supervisione delle chat private di progetto.
 * Contiene informazioni riassuntive di una chat privata, inclusi i dettagli dei partecipanti,
 * l'origine del canale e l'anteprima del messaggio che ha avviato la chat.
 */
public class ChatSupervisioneDTO {

    private int idChat;
    private Timestamp dataCreazioneChat;
    private String creatoreChatUsername; // L'utente che ha avviato la chat
    private String partecipanteChatUsername; // L'altro utente coinvolto
    private String canaleOrigineNome;
    private String messaggioOrigineContenuto;

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
     * Restituisce il timestamp di creazione della chat.
     *
     * @return data e ora di creazione
     */
    public Timestamp getDataCreazioneChat() {
        return dataCreazioneChat;
    }

    /**
     * Imposta il timestamp di creazione della chat.
     *
     * @param dataCreazioneChat data e ora di creazione da assegnare
     */
    public void setDataCreazioneChat(Timestamp dataCreazioneChat) {
        this.dataCreazioneChat = dataCreazioneChat;
    }

    /**
     * Restituisce lo username del creatore della chat.
     *
     * @return username dell'utente iniziatore
     */
    public String getCreatoreChatUsername() {
        return creatoreChatUsername;
    }

    /**
     * Imposta lo username del creatore della chat.
     *
     * @param creatoreChatUsername username dell'utente che ha avviato la chat
     */
    public void setCreatoreChatUsername(String creatoreChatUsername) {
        this.creatoreChatUsername = creatoreChatUsername;
    }

    /**
     * Restituisce lo username del partecipante secondario.
     *
     * @return username dell'altro utente nella chat
     */
    public String getPartecipanteChatUsername() {
        return partecipanteChatUsername;
    }

    /**
     * Imposta lo username del partecipante secondario.
     *
     * @param partecipanteChatUsername username dell'altro utente nell chat
     */
    public void setPartecipanteChatUsername(String partecipanteChatUsername) {
        this.partecipanteChatUsername = partecipanteChatUsername;
    }

    /**
     * Restituisce il nome del canale di origine.
     *
     * @return nome del canale di progetto di origine della chat
     */
    public String getCanaleOrigineNome() {
        return canaleOrigineNome;
    }

    /**
     * Imposta il nome del canale di origine.
     *
     * @param canaleOrigineNome nome del canale di progetto
     */
    public void setCanaleOrigineNome(String canaleOrigineNome) {
        this.canaleOrigineNome = canaleOrigineNome;
    }

    /**
     * Restituisce il contenuto del messaggio di origine.
     *
     * @return anteprima del messaggio che ha avviato la chat
     */
    public String getMessaggioOrigineContenuto() {
        return messaggioOrigineContenuto;
    }

    /**
     * Imposta il contenuto del messaggio di origine.
     *
     * @param messaggioOrigineContenuto anteprima del messaggio iniziale
     */
    public void setMessaggioOrigineContenuto(String messaggioOrigineContenuto) {
        this.messaggioOrigineContenuto = messaggioOrigineContenuto;
    }
}
