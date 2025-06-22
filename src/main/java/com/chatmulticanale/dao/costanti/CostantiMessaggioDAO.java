package com.chatmulticanale.dao.costanti;

public class CostantiMessaggioDAO {
    private CostantiMessaggioDAO() {
        // costruttore privato
    }

    // --- Costanti ---
    public static final String ID_MESSAGGIO = "ID_Messaggio";
    public static final String CONTENUTO = "Contenuto";
    public static final String TIMESTAMP = "Timestamp";
    public static final String MITTENTE_USERNAME = "Mittente_Username";

    // --- Stored Procedures ---
    public static final String SP_GET_MESSAGGI_CANALE = "{CALL sp_UT2_VisualizzaMessaggiCanale(?, ?, ?, ?)}";
    public static final String SP_INVIA_MESSAGGIO_CANALE = "{CALL sp_UT1a_InviaMessaggioCanaleSemplice(?, ?, ?)}";
    public static final String SP_INVIA_MESSAGGIO_CANALE_CITAZIONE = "{CALL sp_UT1b_InviaMessaggioCanaleConCitazione(?, ?, ?, ?)}";
    public static final String SP_INVIA_MESSAGGIO_CHAT = "{CALL sp_UT4a_InviaMessaggioChatPrivataSemplice(?, ?, ?)}";
    public static final String SP_INVIA_MESSAGGIO_CHAT_CITAZIONE = "{CALL sp_UT4b_InviaMessaggioChatPrivataConCitazione(?, ?, ?, ?)}";
    public static final String SP_GET_MESSAGGI_CHAT = "{CALL sp_UT5_VisualizzaMessaggiChatPrivata(?, ?, ?, ?)}";
}
