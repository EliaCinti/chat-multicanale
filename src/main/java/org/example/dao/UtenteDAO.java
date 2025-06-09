package org.example.dao;

import org.example.model.Ruolo;
import org.example.model.Utente;
import org.example.utils.DatabaseConnector;

import java.sql.*;

/**
 * DAO per l'entità Utente. Gestisce l'accesso ai dati degli utenti nel database.
 */
public class UtenteDAO {

    // Costante per la Stored Procedure che recupera l'utente.
    private static final String SP_GET_UTENTE_BY_USERNAME = "{CALL sp_LG1_OttieniUtenteDaUsername(?)}";

    /**
     * Recupera un utente dal database basandosi sul suo username.
     * Questo metodo non verifica la password, ma recupera l'hash per una verifica sicura
     * a livello di applicazione.
     *
     * @param username Lo username da cercare.
     * @return Un oggetto Utente completo di tutti i suoi dati (incluso l'hash), o null se non trovato.
     */
    public Utente getUtenteByUsername(String username) {
        Connection conn = DatabaseConnector.getConnection();
        Utente utente = null;

        // Usiamo un try-with-resources per la gestione automatica delle risorse.
        try (CallableStatement stmt = conn.prepareCall(SP_GET_UTENTE_BY_USERNAME)) {

            // Impostiamo il parametro IN per la Stored Procedure.
            stmt.setString(1, username);

            // Eseguiamo la query e otteniamo un ResultSet.
            try (ResultSet rs = stmt.executeQuery()) {
                // Se c'è almeno un risultato, popoliamo l'oggetto Utente.
                if (rs.next()) {
                    utente = new Utente();
                    utente.setIdUtente(rs.getInt("ID_Utente"));
                    utente.setUsername(rs.getString("Username"));
                    utente.setPassword(rs.getString("Password_Hash")); // Fondamentale per la verifica!
                    utente.setNome(rs.getString("Nome_Utente"));
                    utente.setCognome(rs.getString("Cognome_Utente"));
                    utente.setRuolo(Ruolo.fromString(rs.getString("Ruolo")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'esecuzione della Stored Procedure sp_LG1_OttieniUtenteDaUsername:");
            e.printStackTrace();
            // In futuro, qui lanceremo un'eccezione custom.
        }

        return utente; // Restituisce l'utente trovato o null.
    }

    // Qui in futuro aggiungeremo gli altri metodi:
    // es. public void creaNuovoUtente(...) { ... }
}