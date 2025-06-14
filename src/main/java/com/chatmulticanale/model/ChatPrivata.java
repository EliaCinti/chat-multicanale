package com.chatmulticanale.model;

import java.time.LocalDateTime;

public class ChatPrivata {
    private int idChat;
    private LocalDateTime dataCreazione;
    private int idUtenteCreatore;
    private int idUtentePartecipante;
    private int idMessaggioOrigineChat;

    // --- GETTER E SETTER ---

    public int getIdChat() {
        return idChat;
    }

    public void setIdChat(int idChat) {
        this.idChat = idChat;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public int getIdUtenteCreatore() {
        return idUtenteCreatore;
    }

    public void setIdUtenteCreatore(int idUtenteCreatore) {
        this.idUtenteCreatore = idUtenteCreatore;
    }

    public int getIdUtentePartecipante() {
        return idUtentePartecipante;
    }

    public void setIdUtentePartecipante(int idUtentePartecipante) {
        this.idUtentePartecipante = idUtentePartecipante;
    }

    public int getIdMessaggioOrigineChat() {
        return idMessaggioOrigineChat;
    }

    public void setIdMessaggioOrigineChat(int idMessaggioOrigineChat) {
        this.idMessaggioOrigineChat = idMessaggioOrigineChat;
    }
}
