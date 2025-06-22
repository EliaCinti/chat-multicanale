package com.chatmulticanale.dao;

import com.chatmulticanale.dao.costanti.CostantiUtenteDAO;
import com.chatmulticanale.model.Ruolo;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO per l'entità Utente. Gestisce l'accesso ai dati degli utenti nel database.
 */
public class UtenteDAO {
    // Istanza del logger per questa classe
    private static final Logger logger = Logger.getLogger(UtenteDAO.class.getName());

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
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire creaNuovoUtente perché la connessione al database è assente.");
            throw new SQLException("Connessione al database non disponibile.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(CostantiUtenteDAO.INSERT_UTENTE_QUERY)) {
            // Impostiamo i parametri in ordine, corrispondenti ai '?' nella query.
            stmt.setString(1, nuovoUtente.getUsername());
            stmt.setString(2, nuovoUtente.getPassword());
            stmt.setString(3, nuovoUtente.getNome());
            stmt.setString(4, nuovoUtente.getCognome());
            stmt.setString(5, nuovoUtente.getRuolo().name()); // .name() converte l'Enum in String (es. "dipendente")

            // Eseguiamo l'operazione di inserimento.
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile creare l'utente '" + nuovoUtente.getUsername() + "'. Causa: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera un utente dal database basandosi sul suo username.
     * <p>
     * Questo metodo chiama la Stored Procedure {@code sp_LG1_OttieniUtenteDaUsername}.
     * Non verifica la password, ma recupera l'hash per una verifica sicura
     * a livello di applicazione (es. Nel LoginController).
     * In caso di errore SQL o di connessione assente, logga l'errore e restituisce null.
     *
     * @param username Lo username dell'utente da cercare.
     * @return Un oggetto {@link Utente} completo di tutti i suoi dati se trovato, altrimenti {@code null}.
     */
    public Utente getUtenteByUsername(String username) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getUtenteByUsername perché la connessione al database è assente.");
            return null;
        }

        Utente utente = null;
        try (CallableStatement stmt = conn.prepareCall(CostantiUtenteDAO.SP_GET_UTENTE_BY_USERNAME)) {

            // Impostiamo il parametro IN per la Stored Procedure.
            stmt.setString(1, username);

            // Eseguiamo la query e otteniamo un ResultSet.
            try (ResultSet rs = stmt.executeQuery()) {
                // Se c'è almeno un risultato, popoliamo l'oggetto Utente.
                if (rs.next()) {
                    utente = new Utente();
                    utente.setIdUtente(rs.getInt(CostantiUtenteDAO.ID_UTENTE));
                    utente.setUsername(rs.getString(CostantiUtenteDAO.USERNAME));
                    utente.setPassword(rs.getString(CostantiUtenteDAO.PASSWORD_HASH));
                    utente.setNome(rs.getString(CostantiUtenteDAO.NOME_UTENTE));
                    utente.setCognome(rs.getString(CostantiUtenteDAO.COGNOME_UTENTE));
                    utente.setRuolo(Ruolo.fromString(rs.getString(CostantiUtenteDAO.RUOLO)));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante l'esecuzione della Stored Procedure sp_LG1 per l'utente: " + username);
        }
        return utente; // Restituisce l'utente trovato o null.
    }

    /**
     * Modifica il ruolo di un utente esistente in 'CapoProgetto'.
     * <p>
     * Questo metodo chiama la Stored Procedure {@code sp_AM1_AssegnaRuoloCapoProgetto}.
     * Se la connessione al database fallisce o si verifica un errore SQL durante
     * l'operazione, viene lanciata una {@code SQLException} per notificare il chiamante.
     *
     * @param idUtente L'ID dell'utente da promuovere.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
     */
    public void assegnaRuoloCapoProgetto(int idUtente) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire assegnaRuoloCapoProgetto: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiUtenteDAO.SP_ASSEGNA_RUOLO_CAPOPROGETTO)) {
            stmt.setInt(1, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile promuovere l'utente con ID: " + idUtente, e);
        }
    }

    /**
     * Modifica il ruolo di un utente 'CapoProgetto' riportandolo a 'Dipendente'.
     * <p>
     * Questo metodo chiama la Stored Procedure {@code sp_AM2_RimuoviRuoloCapoProgetto}.
     * Se la connessione al database fallisce o si verifica un errore SQL durante
     * l'operazione, viene lanciata una {@code SQLException} per notificare il chiamante.
     *
     * @param idUtente L'ID dell'utente da "degradare".
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
     */
    public void rimuoviRuoloCapoProgetto(int idUtente) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire rimuoviRuoloCapoProgetto: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiUtenteDAO.SP_RIMUOVI_RUOLO_CAPOPROGETTO)) {
            stmt.setInt(1, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile degradare l'utente con ID: " + idUtente, e);
        }
    }

    /**
     * Recupera una lista di tutti gli utenti con il ruolo 'Dipendente'.
     * <p>
     * Questo metodo esegue una query diretta per selezionare gli utenti.
     * In caso di errore SQL o di connessione assente, logga l'errore e
     * restituisce una lista vuota per garantire che il chiamante non riceva un {@code null}.
     *
     * @return Una {@code List<Utente>} contenente tutti i dipendenti trovati.
     *         La lista sarà vuota se non ci sono dipendenti o in caso di errore.
     */
    public List<Utente> getTuttiDipendenti() {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getTuttiDipendenti: connessione al database assente.");
            return Collections.emptyList();
        }

        List<Utente> dipendenti = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(CostantiUtenteDAO.SELECT_DIPENDENTI_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Utente utente = new Utente();
                utente.setIdUtente(rs.getInt(CostantiUtenteDAO.ID_UTENTE));
                utente.setNome(rs.getString(CostantiUtenteDAO.NOME_UTENTE));
                utente.setCognome(rs.getString(CostantiUtenteDAO.COGNOME_UTENTE));
                dipendenti.add(utente);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero della lista dei dipendenti", e);
        }
        return dipendenti;
    }

    /**
     * Recupera dal database una lista di tutti gli utenti con il ruolo 'CapoProgetto'.
     * <p>
     * Esegue una query diretta per selezionare gli utenti. In caso di errore SQL o di
     * connessione assente, logga l'errore e restituisce una lista vuota.
     *
     * @return Una {@code List<Utente>} contenente tutti i Capi Progetto.
     *         La lista sarà vuota se non ce ne sono o in caso di errore.
     */
    public List<Utente> getTuttiCapiProgetto() {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getTuttiCapiProgetto: connessione al database assente.");
            return Collections.emptyList();
        }

        List<Utente> capiProgetto = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(CostantiUtenteDAO.SELECT_CAPIPROGETTO_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Utente utente = new Utente();
                utente.setIdUtente(rs.getInt(CostantiUtenteDAO.ID_UTENTE));
                utente.setNome(rs.getString(CostantiUtenteDAO.NOME_UTENTE));
                utente.setCognome(rs.getString(CostantiUtenteDAO.COGNOME_UTENTE));
                capiProgetto.add(utente);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero della lista dei Capi Progetto", e);
        }
        return capiProgetto;
    }

    /**
     * Recupera una lista di tutti gli utenti con ruolo 'Dipendente' che non sono
     * ancora membri di un canale specifico.
     * <p>
     * Utile per popolare le liste di utenti che possono essere aggiunti a un canale.
     * In caso di errore SQL o di connessione assente, logga l'errore e restituisce una lista vuota.
     *
     * @param idCanale L'ID del canale da cui escludere i membri attuali.
     * @return Una {@code List<Utente>} di dipendenti che possono essere aggiunti al canale.
     */
    public List<Utente> getDipendentiNonInCanale(int idCanale) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getDipendentiNonInCanale: connessione al database assente.");
            return Collections.emptyList();
        }

        List<Utente> dipendenti = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(CostantiUtenteDAO.SELECT_DIPENDENTI_FUORI_DA_CANALE)) {
            stmt.setInt(1, idCanale);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Utente utente = new Utente();
                    utente.setIdUtente(rs.getInt(CostantiUtenteDAO.ID_UTENTE));
                    utente.setNome(rs.getString(CostantiUtenteDAO.NOME_UTENTE));
                    utente.setCognome(rs.getString(CostantiUtenteDAO.COGNOME_UTENTE));
                    dipendenti.add(utente);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei dipendenti non nel canale ID: " + idCanale);        }
        return dipendenti;
    }

    /**
     * Recupera una lista di tutti gli utenti con ruolo 'Dipendente' che sono
     * attualmente membri di un canale specifico.
     * <p>
     * Utile per popolare le liste di utenti che possono essere rimossi da un canale.
     * In caso di errore SQL o di connessione assente, logga l'errore e restituisce una lista vuota.
     *
     * @param idCanale L'ID del canale di cui elencare i membri.
     * @return Una {@code List<Utente>} di dipendenti presenti nel canale.
     */
    public List<Utente> getDipendentiInCanale(int idCanale) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getDipendentiInCanale: connessione al database assente.");
            return Collections.emptyList();
        }

        List<Utente> dipendenti = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(CostantiUtenteDAO.SELECT_DIPENDENTI_IN_CANALE)) {
            stmt.setInt(1, idCanale);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Utente utente = new Utente();
                    utente.setIdUtente(rs.getInt(CostantiUtenteDAO.ID_UTENTE));
                    utente.setNome(rs.getString(CostantiUtenteDAO.NOME_UTENTE));
                    utente.setCognome(rs.getString(CostantiUtenteDAO.COGNOME_UTENTE));
                    dipendenti.add(utente);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei dipendenti nel canale ID: " + idCanale);        }
        return dipendenti;
    }
}