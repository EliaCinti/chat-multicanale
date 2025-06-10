package com.chatmulticanale.dao;

import com.chatmulticanale.model.Ruolo;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.DatabaseConnector;

import java.sql.*;

/**
 * DAO per l'entità Utente. Gestisce l'accesso ai dati degli utenti nel database.
 */
public class UtenteDAO {

    // Costante per la Stored Procedure che recupera l'utente.
    private static final String SP_GET_UTENTE_BY_USERNAME = "{CALL sp_LG1_OttieniUtenteDaUsername(?)}";

    // Definiamo la query SQL come una costante privata per pulizia e sicurezza.
    private static final String INSERT_UTENTE_QUERY = "INSERT INTO Utente (Username, Password_Hash, Nome_Utente, Cognome_Utente, Ruolo) VALUES (?, ?, ?, ?, ?)";

    /**
     * Inserisce un nuovo utente nel database usando un comando INSERT diretto,
     * come consentito dai privilegi documentati per il ruolo applicativo.
     * Questo metodo non usa una Stored Procedure.
     *
     * @param nuovoUtente L'oggetto Utente con tutti i dati da salvare.
     * @throws SQLException Se l'username esiste già (violazione del vincolo UNIQUE) o per altri errori DB.
     */
    public void creaNuovoUtente(Utente nuovoUtente) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();

        // Usiamo un PreparedStatement per eseguire query SQL dirette in modo sicuro.
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_UTENTE_QUERY)) {

            // Impostiamo i parametri in ordine, corrispondenti ai '?' nella query.
            stmt.setString(1, nuovoUtente.getUsername());
            stmt.setString(2, nuovoUtente.getPassword());
            stmt.setString(3, nuovoUtente.getNome());
            stmt.setString(4, nuovoUtente.getCognome());
            stmt.setString(5, nuovoUtente.getRuolo().name()); // .name() converte l'Enum in String (es. "dipendente")

            // Eseguiamo l'operazione di inserimento.
            stmt.executeUpdate();
        }
    }

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