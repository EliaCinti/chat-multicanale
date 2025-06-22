package com.chatmulticanale.dao.costanti;

public class CostantiCanaleProgettoDAO {

    private CostantiCanaleProgettoDAO() {
        // costruttore vuoto
    }

    // --- Costanti ---
    public static final String ID_CANALE = "ID_Canale";
    public static final String NOME_CANALE = "Nome_Canale";
    public static final String DESCRIZIONE_CANALE = "Descrizione_Canale";

    // --- Stored Procedures ---
    public static final String SP_CREA_CANALE = "{CALL sp_CP1_CreaCanaleProgetto(?, ?, ?, ?)}";
    public static final String SP_GET_CANALI_PARTECIPATI = "{CALL sp_UT6_VisualizzaElencoCanaliPartecipati(?)}";

    // --- Query Dirette ---
    public static final String SELECT_CANALI_DI_PROGETTO = "SELECT ID_Canale, Nome_Canale FROM CanaleProgetto WHERE Progetto = ?";

}
