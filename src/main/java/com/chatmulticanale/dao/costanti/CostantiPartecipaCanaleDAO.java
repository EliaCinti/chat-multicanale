package com.chatmulticanale.dao.costanti;

public class CostantiPartecipaCanaleDAO {

    private CostantiPartecipaCanaleDAO() {
        // costruttore privato
    }
    // --- Stored Procedures ---
    public static final String SP_AGGIUNGI_UTENTE_A_CANALE = "{CALL sp_CP2_AggiungiUtenteACanale(?, ?)}";
    public static final String SP_RIMUOVI_UTENTE_DA_CANALE = "{CALL sp_CP3_RimuoviUtenteDaCanale(?, ?)}";
}
