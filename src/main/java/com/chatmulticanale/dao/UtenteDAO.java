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
 * DAO per l'entità {@link Utente}. Gestisce l'accesso ai dati degli utenti nel database.
 * Offre metodi per creare, leggere e aggiornare informazioni sugli utenti,
 * inclusa la gestione dei ruoli e delle membership nei canali.
 * Utilizza comandi SQL e Stored Procedures definite in {@link CostantiUtenteDAO}
 * e connessioni fornite da {@link DatabaseConnector}.
 */
public class UtenteDAO {
    private static final Logger logger = Logger.getLogger(UtenteDAO.class.getName());

    /**
     * Inserisce un nuovo utente nel database tramite INSERT diretto.
     * Non utilizza Stored Procedure, rispettando i privilegi documentati.
     *
     * @param nuovoUtente oggetto {@link Utente} contenente username, password hash, nome, cognome e ruolo
     * @throws SQLException se la connessione non è disponibile o l'username viola vincoli UNIQUE
     * @see CostantiUtenteDAO#INSERT_UTENTE_QUERY
     * @see DatabaseConnector#getConnection()
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
     * Recupera un utente dal database in base allo username.
     * Utilizza la Stored Procedure {@code sp_LG1_OttieniUtenteDaUsername}.
     * Non verifica la password, ma restituisce l'hash per la validazione esterna.
     *
     * @param username username dell'utente da recuperare
     * @return oggetto {@link Utente} popolato se trovato, {@code null} altrimenti
     * @see CostantiUtenteDAO#SP_GET_UTENTE_BY_USERNAME
     * @see DatabaseConnector#getConnection()
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
     * Promuove un utente al ruolo di 'CapoProgetto'.
     * Utilizza la Stored Procedure {@code sp_AM1_AssegnaRuoloCapoProgetto}.
     *
     * @param idUtente identificativo dell'utente da promuovere
     * @throws SQLException se la connessione non è disponibile o si verifica un errore SQL
     * @see CostantiUtenteDAO#SP_ASSEGNA_RUOLO_CAPOPROGETTO
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
     * Degrada un utente dal ruolo di 'CapoProgetto' a 'Dipendente'.
     * Utilizza la Stored Procedure {@code sp_AM2_RimuoviRuoloCapoProgetto}.
     *
     * @param idUtente identificativo dell'utente da degradare
     * @throws SQLException se la connessione non è disponibile o si verifica un errore SQL
     * @see CostantiUtenteDAO#SP_RIMUOVI_RUOLO_CAPOPROGETTO
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
     * Recupera tutti gli utenti con ruolo 'Dipendente'.
     * Esegue una query diretta e restituisce lista vuota in caso di errore.
     *
     * @return lista di {@link Utente} con ruolo dipendente, o lista vuota se nessuno o errore
     * @see CostantiUtenteDAO#SELECT_DIPENDENTI_QUERY
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
     * Recupera tutti gli utenti con ruolo 'CapoProgetto'.
     * Esegue una query diretta e restituisce lista vuota in caso di errore.
     *
     * @return lista di {@link Utente} con ruolo capo progetto, o lista vuota se nessuno o errore
     * @see CostantiUtenteDAO#SELECT_CAPIPROGETTO_QUERY
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
     * Recupera dipendenti non ancora membri di un dato canale.
     * Utile per aggiunte a canali di progetto.
     *
     * @param idCanale identificativo del canale da escludere
     * @return lista di {@link Utente} dipendenti non membri, o lista vuota se errore
     * @see CostantiUtenteDAO#SELECT_DIPENDENTI_FUORI_DA_CANALE
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
     * Recupera dipendenti attualmente membri di un dato canale.
     * Utile per rimozioni da canali di progetto.
     *
     * @param idCanale identificativo del canale
     * @return lista di {@link Utente} dipendenti membri, o lista vuota se errore
     * @see CostantiUtenteDAO#SELECT_DIPENDENTI_IN_CANALE
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