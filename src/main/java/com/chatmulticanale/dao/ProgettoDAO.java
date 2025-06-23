package com.chatmulticanale.dao;

import com.chatmulticanale.dao.costanti.CostantiProgettoDAO;
import com.chatmulticanale.dao.costanti.CostantiUtenteDAO;
import com.chatmulticanale.dto.ProgettoResponsabileDTO;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO per la gestione dei progetti nel database.
 * Fornisce metodi per:
 * <ul>
 *   <li>Recuperare progetti con i loro responsabili (DTO)</li>
 *   <li>Gestire operazioni CRUD sui progetti</li>
 *   <li>Assegnare e riassegnare responsabilità</li>
 * </ul>
 * Utilizza costanti SQL definite in {@link CostantiProgettoDAO} e
 * {@link CostantiUtenteDAO}, e connessioni tramite {@link DatabaseConnector}.
 */
public class ProgettoDAO {
    private static final Logger logger = Logger.getLogger(ProgettoDAO.class.getName());

    /**
     * Restituisce i progetti con i relativi responsabili.
     * Esegue un JOIN efficiente per aggregare i dati.
     *
     * @return lista di {@link ProgettoResponsabileDTO}; lista vuota in caso di errore
     * @see CostantiProgettoDAO#SELECT_PROGETTI_E_RESPONSABILI_QUERY
     * @see DatabaseConnector#getConnection()
     */
    public List<ProgettoResponsabileDTO> getProgettiConResponsabile() {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getProgettiConResponsabile: connessione al database assente.");
            return Collections.emptyList();
        }

        List<ProgettoResponsabileDTO> risultati = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(CostantiProgettoDAO.SELECT_PROGETTI_E_RESPONSABILI_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProgettoResponsabileDTO dto = new ProgettoResponsabileDTO();
                dto.setIdProgetto(rs.getInt(CostantiProgettoDAO.ID_PROGETTO));
                dto.setNomeProgetto(rs.getString(CostantiProgettoDAO.NOME_PROGETTO));
                dto.setIdResponsabile(rs.getInt(CostantiUtenteDAO.ID_UTENTE));
                dto.setNomeResponsabile(rs.getString(CostantiUtenteDAO.NOME_UTENTE));
                dto.setCognomeResponsabile(rs.getString(CostantiUtenteDAO.COGNOME_UTENTE));
                risultati.add(dto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei progetti con responsabile", e);
        }
        return risultati;
    }

    /**
     * Recupera i progetti di cui un utente è responsabile.
     *
     * @param idResponsabile identificativo dell'utente responsabile
     * @return lista di {@link Progetto}; lista vuota se nessun progetto o errore
     * @see CostantiProgettoDAO#SELECT_PROGETTI_RESPONSABILE_QUERY
     */
    public List<Progetto> trovaProgettiPerResponsabile(int idResponsabile) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire trovaProgettiPerResponsabile: connessione al database assente.");
            return Collections.emptyList();
        }

        List<Progetto> progetti = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(CostantiProgettoDAO.SELECT_PROGETTI_RESPONSABILE_QUERY)) {
            stmt.setInt(1, idResponsabile);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Progetto progetto = new Progetto();
                    progetto.setIdProgetto(rs.getInt(CostantiProgettoDAO.ID_PROGETTO));
                    progetto.setNomeProgetto(rs.getString(CostantiProgettoDAO.NOME_PROGETTO));
                    progetti.add(progetto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore nel trovare i progetti per il responsabile ID: " + idResponsabile);
        }
        return progetti;
    }

    /**
     * Assegna la responsabilità di un progetto a un capo progetto.
     * Utilizza la Stored Procedure {@code sp_AM3_AssegnaResponsabilitaProgetto}.
     *
     * @param idProgetto        identificativo del progetto
     * @param idCapoProgetto    identificativo del capo progetto
     * @throws SQLException in caso di connessione assente o errore SQL
     * @see CostantiProgettoDAO#SP_ASSEGNA_RESPONSABILITA_PROGETTO
     */
    public void assegnaResponsabilitaProgetto(int idProgetto, int idCapoProgetto) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire assegnaResponsabilitaProgetto: connessione al database assente.");
        }
        try (CallableStatement stmt = conn.prepareCall(CostantiProgettoDAO.SP_ASSEGNA_RESPONSABILITA_PROGETTO)) {
            stmt.setInt(1, idProgetto);
            stmt.setInt(2, idCapoProgetto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile assegnare il progetto ID: " + idProgetto, e);
        }
    }

    /**
     * Riassegna un progetto a un nuovo responsabile.
     * Utilizza la Stored Procedure {@code sp_AM4_RiassegnaResponsabilitaProgetto}.
     *
     * @param idProgetto           identificativo del progetto
     * @param idNuovoResponsabile  identificativo del nuovo responsabile
     * @throws SQLException in caso di connessione assente o errore SQL
     * @see CostantiProgettoDAO#SP_RIASSEGNA_RESPONSABILITA_PROGETTI
     */
    public void aggiornaResponsabileProgetto(int idProgetto, int idNuovoResponsabile) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire aggiornaResponsabileProgetto: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiProgettoDAO.SP_RIASSEGNA_RESPONSABILITA_PROGETTI)) {
            stmt.setInt(1, idProgetto);
            stmt.setInt(2, idNuovoResponsabile);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile riassegnare il progetto ID: " + idProgetto, e);
        }
    }

    /**
     * Crea un nuovo progetto nel database con nome e descrizione.
     *
     * @param nuovoProgetto oggetto {@link Progetto} con i dati del progetto
     * @throws SQLException in caso di connessione assente o errore SQL (es. Duplicato)
     * @see CostantiProgettoDAO#INSERT_PROGETTO_QUERY
     */
    public void creaNuovoProgetto(Progetto nuovoProgetto) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire creaNuovoProgetto: connessione al database assente.");
        }

        try (PreparedStatement stmt = conn.prepareStatement(CostantiProgettoDAO.INSERT_PROGETTO_QUERY)) {
            stmt.setString(1, nuovoProgetto.getNomeProgetto());
            stmt.setString(2, nuovoProgetto.getDescrizioneProgetto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile creare il progetto '" + nuovoProgetto.getNomeProgetto() + "'", e);        }
    }

    /**
     * Recupera tutti i progetti privi di responsabile.
     * Utile per operazioni di assegnazione amministrativa.
     *
     * @return lista di {@link Progetto}; lista vuota in caso di errore
     * @see CostantiProgettoDAO#SELECT_PROGETTI_NON_ASSEGNATI_QUERY
     */
    public List<Progetto> getProgettiNonAssegnati() {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getProgettiNonAssegnati: connessione al database assente.");
            return Collections.emptyList();
        }

        List<Progetto> progetti = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(CostantiProgettoDAO.SELECT_PROGETTI_NON_ASSEGNATI_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Progetto progetto = new Progetto();
                progetto.setIdProgetto(rs.getInt(CostantiProgettoDAO.ID_PROGETTO));
                progetto.setNomeProgetto(rs.getString(CostantiProgettoDAO.NOME_PROGETTO));
                progetti.add(progetto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei progetti non assegnati", e);
        }
        return progetti;
    }
}