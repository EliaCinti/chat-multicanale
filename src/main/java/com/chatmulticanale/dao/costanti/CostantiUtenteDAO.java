package com.chatmulticanale.dao.costanti;

public class CostantiUtenteDAO {

    private CostantiUtenteDAO() {
        // costruttore vuoto
    }

    // --- Costanti ---
    public static final String ID_UTENTE = "ID_Utente";
    public static final String USERNAME = "Username";
    public static final String PASSWORD_HASH = "Password_Hash";
    public static final String NOME_UTENTE = "Nome_Utente";
    public static final String COGNOME_UTENTE = "Cognome_Utente";
    public static final String RUOLO = "Ruolo";

    // --- Stored Procedures ---
    public static final String SP_GET_UTENTE_BY_USERNAME = "{CALL sp_LG1_OttieniUtenteDaUsername(?)}";
    public static final String SP_ASSEGNA_RUOLO_CAPOPROGETTO = "{CALL sp_AM1_AssegnaRuoloCapoProgetto(?)}";
    public static final String SP_RIMUOVI_RUOLO_CAPOPROGETTO = "{CALL sp_AM2_RimuoviRuoloCapoProgetto(?)}";

    // --- Query Dirette ---
    public static final String INSERT_UTENTE_QUERY = "INSERT INTO Utente (Username, Password_Hash, Nome_Utente, Cognome_Utente, Ruolo) VALUES (?, ?, ?, ?, ?)";
    public static final String SELECT_DIPENDENTI_QUERY = "SELECT ID_Utente, Nome_Utente, Cognome_Utente FROM Utente WHERE Ruolo = 'Dipendente'";
    public static final String SELECT_CAPIPROGETTO_QUERY = "SELECT ID_Utente, Nome_Utente, Cognome_Utente FROM Utente WHERE Ruolo = 'CapoProgetto'";
    public static final String SELECT_DIPENDENTI_FUORI_DA_CANALE =
            "SELECT ID_Utente, Nome_Utente, Cognome_Utente " +
                    "FROM Utente " +
                    "WHERE Ruolo = 'Dipendente' AND ID_Utente NOT IN (SELECT ID_Utente FROM PartecipaCanale WHERE ID_Canale = ?)";
    public static final String SELECT_DIPENDENTI_IN_CANALE =
            "SELECT u.ID_Utente, u.Nome_Utente, u.Cognome_Utente " +
                    "FROM Utente u JOIN PartecipaCanale pc ON u.ID_Utente = pc.ID_Utente " +
                    "WHERE pc.ID_Canale = ? AND u.Ruolo = 'Dipendente'";

}
