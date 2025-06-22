package com.chatmulticanale.dao.costanti;

public class CostantiProgettoDAO {

    private CostantiProgettoDAO() {
        // costruttore vuoto
    }

    // --- Costanti ---
    public static final String ID_PROGETTO = "ID_Progetto";
    public static final String NOME_PROGETTO = "Nome_Progetto";

    // --- Stored Procedures ---
    public static final String SP_RIASSEGNA_RESPONSABILITA_PROGETTI = "{CALL sp_AM4_RiassegnaResponsabilitaProgetto(?, ?)}";
    public static final String SP_ASSEGNA_RESPONSABILITA_PROGETTO = "{CALL sp_AM3_AssegnaResponsabilitaProgetto(?, ?)}";

    // --- Query Dirette ---
    public static final String SELECT_PROGETTI_RESPONSABILE_QUERY = "SELECT ID_Progetto, Nome_Progetto FROM Progetto WHERE Utente_Responsabile = ?";
    public static final String INSERT_PROGETTO_QUERY = "INSERT INTO Progetto (Nome_Progetto, Descrizione_Progetto) VALUES (?, ?)";
    public static final String SELECT_PROGETTI_NON_ASSEGNATI_QUERY = "SELECT ID_Progetto, Nome_Progetto FROM Progetto WHERE Utente_Responsabile IS NULL";
    public static final String SELECT_PROGETTI_E_RESPONSABILI_QUERY =
            "SELECT p.ID_Progetto, p.Nome_Progetto, u.ID_Utente, u.Nome_Utente, u.Cognome_Utente " +
                    "FROM Progetto p JOIN Utente u ON p.Utente_Responsabile = u.ID_Utente";
}
