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

public class ProgettoDAO {
    private static final Logger logger = Logger.getLogger(ProgettoDAO.class.getName());

    /**
     * Recupera una lista di DTO contenenti i progetti assegnati e i dettagli dei loro responsabili,
     * incluso l'ID del responsabile.
     * <p>
     * Esegue una sola query efficiente con un {@code JOIN} per ottenere tutti i dati necessari.
     * In caso di errore, logga e restituisce una lista vuota.
     *
     * @return Una {@code List<ProgettoResponsabileDTO>} con i dati dei progetti e dei loro manager.
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
     * Trova tutti i progetti di cui un dato utente è responsabile.
     *
     * @param idResponsabile L'ID dell'utente responsabile.
     * @return Una {@code List<Progetto>} contenente i progetti gestiti dall'utente.
     *         La lista sarà vuota se l'utente non gestisce progetti o in caso di errore.
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
     * Assegna per la prima volta un responsabile a un progetto.
     * <p>
     * Utilizza la Stored Procedure {@code sp_AM3_AssegnaResponsabilitaProgetto}.
     *
     * @param idProgetto L'ID del progetto da assegnare.
     * @param idCapoProgetto L'ID del Capo Progetto a cui assegnarlo.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
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
     * Aggiorna il responsabile di un progetto esistente.
     * <p>
     * Utilizza la Stored Procedure {@code sp_AM4_RiassegnaResponsabilitaProgetto}.
     *
     * @param idProgetto L'ID del progetto da aggiornare.
     * @param idNuovoResponsabile L'ID del nuovo utente responsabile.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
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
     * Inserisce un nuovo progetto nel database.
     * <p>
     * Il campo {@code Utente_Responsabile} viene lasciato a {@code NULL} di default e può
     * essere assegnato in un secondo momento.
     *
     * @param nuovoProgetto Un oggetto {@link Progetto} contenente il nome e la descrizione.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore
     *         (es. Nome progetto duplicato).
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
     * Recupera una lista di tutti i progetti che non hanno un responsabile assegnato.
     * <p>
     * Questo metodo è fondamentale per l'operazione amministrativa di assegnazione (AM3).
     * In caso di errore SQL o di connessione assente, logga l'errore e restituisce una lista vuota.
     *
     * @return Una {@code List<Progetto>} (potenzialmente vuota) di progetti non assegnati.
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