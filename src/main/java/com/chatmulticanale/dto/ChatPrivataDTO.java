package com.chatmulticanale.dto;

import java.sql.Timestamp;

public class ChatPrivataDTO {

    private int idChat;
    private Timestamp dataCreazione;
    private String altroPartecipanteUsername;

    // --- GETTER E SETTER ---

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getAltroPartecipanteUsername() {
        return altroPartecipanteUsername;
    }

    public void setAltroPartecipanteUsername(String altroPartecipanteUsername) {
        this.altroPartecipanteUsername = altroPartecipanteUsername;
    }
}