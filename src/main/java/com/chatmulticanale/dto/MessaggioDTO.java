package com.chatmulticanale.dto;

import java.sql.Timestamp;

public class MessaggioDTO {

    private int idMessaggio;
    private String contenuto;
    private Timestamp timestamp;
    private String usernameMittente;
    private Integer idMessaggioCitato;

    // --- GETTER E SETTER ---

    public int getIdMessaggio() {
        return idMessaggio;
    }

    public void setIdMessaggio(int idMessaggio) {
        this.idMessaggio = idMessaggio;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsernameMittente() {
        return usernameMittente;
    }

    public void setUsernameMittente(String usernameMittente) {
        this.usernameMittente = usernameMittente;
    }

    public Integer getIdMessaggioCitato() {
        return idMessaggioCitato;
    }

    public void setIdMessaggioCitato(Integer idMessaggioCitato) {
        this.idMessaggioCitato = idMessaggioCitato;
    }
}