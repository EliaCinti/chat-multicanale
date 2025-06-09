package org.example.model;

import java.time.LocalDateTime;

public class Messaggio {
    private int idMessaggio;
    private String contenuto;
    private LocalDateTime timestamp;
    private int idCanaleRiferimento;
    private int idChatRiferimento;
    private int idMessaggioCitato;
    private int idUtenteMittente;

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getIdCanaleRiferimento() {
        return idCanaleRiferimento;
    }

    public void setIdCanaleRiferimento(int idCanaleRiferimento) {
        this.idCanaleRiferimento = idCanaleRiferimento;
    }

    public int getIdChatRiferimento() {
        return idChatRiferimento;
    }

    public void setIdChatRiferimento(int idChatRiferimento) {
        this.idChatRiferimento = idChatRiferimento;
    }

    public int getIdMessaggioCitato() {
        return idMessaggioCitato;
    }

    public void setIdMessaggioCitato(int idMessaggioCitato) {
        this.idMessaggioCitato = idMessaggioCitato;
    }

    public int getIdUtenteMittente() {
        return idUtenteMittente;
    }

    public void setIdUtenteMittente(int idUtenteMittente) {
        this.idUtenteMittente = idUtenteMittente;
    }
}
