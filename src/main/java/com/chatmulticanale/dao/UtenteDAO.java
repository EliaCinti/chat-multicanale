package com.chatmulticanale.dao;

import com.chatmulticanale.model.Ruolo;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO per l'entità Utente. Gestisce l'accesso ai dati degli utenti nel database.
 */
public class UtenteDAO {
    // Istanza del logger per questa classe
    private static final Logger logger = Logger.getLogger(UtenteDAO.class.getName());

    // --- Stored Procedures ---
    private static final String SP_GET_UTENTE_BY_USERNAME = "{CALL sp_LG1_OttieniUtenteDaUsername(?)}";
    private static final String SP_ASSEGNA_RUOLO_CAPOPROGETTO = "{CALL sp_AM1_AssegnaRuoloCapoProgetto(?)}";
    private static final String SP_RIMUOVI_RUOLO_CAPOPROGETTO = "{CALL sp_AM2_RimuoviRuoloCapoProgetto(?)}";

    // --- Query Dirette ---
    private static final String INSERT_UTENTE_QUERY = "INSERT INTO Utente (Username, Password_Hash, Nome_Utente, Cognome_Utente, Ruolo) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_DIPENDENTI_QUERY = "SELECT ID_Utente, Nome_Utente, Cognome_Utente FROM Utente WHERE Ruolo = 'Dipendente'";
    private static final String SELECT_CAPIPROGETTO_QUERY = "SELECT ID_Utente, Nome_Utente, Cognome_Utente FROM Utente WHERE Ruolo = 'CapoProgetto'";

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
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_UTENTE_QUERY)) {

            // Impostiamo i parametri in ordine, corrispondenti ai '?' nella query.
            stmt.setString(1, nuovoUtente.getUsername());
            stmt.setString(2, nuovoUtente.getPassword());
            stmt.setString(3, nuovoUtente.getNome());
            stmt.setString(4, nuovoUtente.getCognome());
            stmt.setString(5, nuovoUtente.getRuolo().name()); // .name() converte l'Enum in String (es. "dipendente")

            // Eseguiamo l'operazione di inserimento.
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore SQL durante la creazione dell'utente: " + nuovoUtente.getUsername(), e);
            throw e;
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
                    utente.setPassword(rs.getString("Password_Hash"));
                    utente.setNome(rs.getString("Nome_Utente"));
                    utente.setCognome(rs.getString("Cognome_Utente"));
                    utente.setRuolo(Ruolo.fromString(rs.getString("Ruolo")));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'esecuzione della Stored Procedure sp_LG1_OttieniUtenteDaUsername per l'utente: " + username, e);
        }

        return utente; // Restituisce l'utente trovato o null.
    }

    /**
     * Modifica il ruolo di un utente esistente in 'CapoProgetto'. Chiama la SP AM1.
     * @param idUtente L'ID dell'utente da promuovere.
     * @throws SQLException in caso di errore del database.
     */
    public void assegnaRuoloCapoProgetto(int idUtente) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_ASSEGNA_RUOLO_CAPOPROGETTO)) {
            stmt.setInt(1, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'esecuzione di sp_AM1_AssegnaRuoloCapoProgetto per l'ID utente: " + idUtente, e);
            throw e; // Rilancia l'eccezione
        }
    }

    /**
     * Modifica il ruolo di un utente 'CapoProgetto' in 'Dipendente'. Chiama la SP AM2.
     * @param idUtente L'ID dell'utente da "degradare".
     * @throws SQLException in caso di errore del database.
     */
    public void rimuoviRuoloCapoProgetto(int idUtente) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_RIMUOVI_RUOLO_CAPOPROGETTO)) {
            stmt.setInt(1, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'esecuzione di sp_AM2_RimuoviRuoloCapoProgetto per l'ID utente: " + idUtente, e);
            throw e;
        }
    }

    /**
     * Recupera una lista di tutti gli utenti con il ruolo 'Dipendente'.
     * @return Una lista di oggetti Utente (ID, Nome, Cognome).
     */
    public List<Utente> getTuttiDipendenti() {
        List<Utente> dipendenti = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(SELECT_DIPENDENTI_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setIdUtente(rs.getInt("ID_Utente"));
                utente.setNome(rs.getString("Nome_Utente"));
                utente.setCognome(rs.getString("Cognome_Utente"));
                dipendenti.add(utente);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero della lista dei dipendenti", e);
        }
        return dipendenti;
    }

    /**
     * Recupera dal database una lista di tutti gli utenti con il ruolo 'CapoProgetto'.
     * @return Una lista di oggetti Utente (ID, Nome, Cognome).
     */
    public List<Utente> getTuttiCapiProgetto() {
        List<Utente> capiProgetto = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(SELECT_CAPIPROGETTO_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Utente utente = new Utente();
                utente.setIdUtente(rs.getInt("ID_Utente"));
                utente.setNome(rs.getString("Nome_Utente"));
                utente.setCognome(rs.getString("Cognome_Utente"));
                capiProgetto.add(utente);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero della lista dei Capi Progetto", e);
        }
        return capiProgetto;
    }
}