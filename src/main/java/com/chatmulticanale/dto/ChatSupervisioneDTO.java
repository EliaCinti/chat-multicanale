package com.chatmulticanale.dto;

import java.sql.Timestamp;

public class ChatSupervisioneDTO {

    private int idChat;
    private Timestamp dataCreazioneChat;
    private String creatoreChatUsername; // L'utente che ha avviato la chat
    private String partecipanteChatUsername; // L'altro utente coinvolto
    private String canaleOrigineNome;
    private String messaggioOrigineContenuto;

    // --- GETTER E SETTER ---

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    public Timestamp getDataCreazioneChat() {
        return dataCreazioneChat;
    }

    public void setDataCreazioneChat(Timestamp dataCreazioneChat) {
        this.dataCreazioneChat = dataCreazioneChat;
    }

    public String getCreatoreChatUsername() {
        return creatoreChatUsername;
    }

    public void setCreatoreChatUsername(String creatoreChatUsername) {
        this.creatoreChatUsername = creatoreChatUsername;
    }

    public String getPartecipanteChatUsername() {
        return partecipanteChatUsername;
    }

    public void setPartecipanteChatUsername(String partecipanteChatUsername) {
        this.partecipanteChatUsername = partecipanteChatUsername;
    }

    public String getCanaleOrigineNome() {
        return canaleOrigineNome;
    }

    public void setCanaleOrigineNome(String canaleOrigineNome) {
        this.canaleOrigineNome = canaleOrigineNome;
    }

    public String getMessaggioOrigineContenuto() {
        return messaggioOrigineContenuto;
    }

    public void setMessaggioOrigineContenuto(String messaggioOrigineContenuto) {
        this.messaggioOrigineContenuto = messaggioOrigineContenuto;
    }
}
