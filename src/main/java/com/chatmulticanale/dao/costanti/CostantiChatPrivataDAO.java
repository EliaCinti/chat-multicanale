package com.chatmulticanale.dao.costanti;

public class CostantiChatPrivataDAO {

    private CostantiChatPrivataDAO() {
        // costruttore vuoto
    }

    // --- Costanti ---
    public static final String ID_CHAT = "ID_Chat";
    public static final String TIMESTAMP_CREAZIONE = "Timestamp_Creazione";
    public static final String CREATORE_CHAT = "Creatore_Chat_Username";
    public static final String PARTECIPANTE_CHAT = "Partecipante_Chat_Username";
    public static final String NOME_CANALE_ORIGINE = "Canale_Origine_Nome";
    public static final String MESSAGGIO_ORIGINE = "Messaggio_Originale_Contenuto";
    public static final String ALTRO_PARTECIPANTE_USERNAME = "Altro_Partecipante_Username";

    // --- Stored Procedures ---
    public static final String SP_GET_CHAT_DA_SUPERVISIONARE = "{CALL sp_CP4_AccediChatPrivateProgetto(?, ?)}";
    public static final String SP_AVVIA_CHAT_PRIVATA = "{CALL sp_UT3_AvviaChatPrivataDaMessaggio(?, ?)}";
    public static final String SP_GET_CHAT_UTENTE = "{CALL sp_UT7_VisualizzaElencoChatPrivatePersonali(?)}";

}
