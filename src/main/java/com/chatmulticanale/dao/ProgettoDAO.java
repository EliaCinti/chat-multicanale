package com.chatmulticanale.dao;

import com.chatmulticanale.dto.ProgettoResponsabileDTO;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgettoDAO {
    private static final Logger logger = Logger.getLogger(ProgettoDAO.class.getName());

    // --- Stored Procedures ---
    private static final String SP_RIASSEGNA_RESPONSABILITA_PROGETTI = "{CALL sp_AM4_RiassegnaResponsabilitaProgetto(?, ?)}";
    private static final String SP_ASSEGNA_RESPONSABILITA_PROGETTO = "{CALL sp_AM3_AssegnaResponsabilitaProgetto(?, ?)}";

    // --- Query Dirette ---
    private static final String SELECT_PROGETTI_RESPONSABILE_QUERY = "SELECT ID_Progetto, Nome_Progetto FROM Progetto WHERE Utente_Responsabile = ?";
    private static final String INSERT_PROGETTO_QUERY = "INSERT INTO Progetto (Nome_Progetto, Descrizione_Progetto) VALUES (?, ?)";
    private static final String SELECT_PROGETTI_NON_ASSEGNATI_QUERY = "SELECT ID_Progetto, Nome_Progetto FROM Progetto WHERE Utente_Responsabile IS NULL";
    private static final String SELECT_PROGETTI_E_RESPONSABILI_QUERY =
            "SELECT p.ID_Progetto, p.Nome_Progetto, u.ID_Utente, u.Nome_Utente, u.Cognome_Utente " +
                    "FROM Progetto p JOIN Utente u ON p.Utente_Responsabile = u.ID_Utente";

    /**
     * Recupera una lista di DTO contenenti i progetti assegnati e i dettagli dei loro responsabili,
     * incluso l'ID del responsabile.
     * Esegue una sola query efficiente per ottenere tutti i dati necessari.
     * @return Una lista di ProgettoResponsabileDTO.
     */
    public List<ProgettoResponsabileDTO> getProgettiConResponsabile() {
        List<ProgettoResponsabileDTO> risultati = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(SELECT_PROGETTI_E_RESPONSABILI_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProgettoResponsabileDTO dto = new ProgettoResponsabileDTO();
                dto.setIdProgetto(rs.getInt("ID_Progetto"));
                dto.setNomeProgetto(rs.getString("Nome_Progetto"));
                dto.setIdResponsabile(rs.getInt("ID_Utente"));
                dto.setNomeResponsabile(rs.getString("Nome_Utente"));
                dto.setCognomeResponsabile(rs.getString("Cognome_Utente"));
                risultati.add(dto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei progetti con responsabile", e);
        }
        return risultati;
    }

    public List<Progetto> trovaProgettiPerResponsabile(int idResponsabile) {
        List<Progetto> progetti = new ArrayList<>();

        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(SELECT_PROGETTI_RESPONSABILE_QUERY)) {
            stmt.setInt(1, idResponsabile);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Progetto progetto = new Progetto();
                    progetto.setIdProgetto(rs.getInt("ID_Progetto"));
                    progetto.setNomeProgetto(rs.getString("Nome_Progetto"));
                    progetti.add(progetto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore nel trovare i progetti per il responsabile ID: " + idResponsabile, e);
        }
        return progetti;
    }

    /**
     * Assegna per la prima volta un responsabile a un progetto.
     * Utilizza la Stored Procedure sp_AM3.
     *
     * @param idProgetto L'ID del progetto da assegnare.
     * @param idCapoProgetto L'ID del Capo Progetto a cui assegnarlo.
     * @throws SQLException in caso di errore.
     */
    public void assegnaResponsabilitaProgetto(int idProgetto, int idCapoProgetto) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_ASSEGNA_RESPONSABILITA_PROGETTO)) {
            stmt.setInt(1, idProgetto);
            stmt.setInt(2, idCapoProgetto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'assegnazione del progetto ID: " + idProgetto + " al responsabile ID: " + idCapoProgetto, e);
            throw e;
        }
    }

    public void aggiornaResponsabileProgetto(int idProgetto, int idNuovoResponsabile) throws SQLException {

        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_RIASSEGNA_RESPONSABILITA_PROGETTI)) {
            stmt.setInt(1, idProgetto);
            stmt.setInt(2, idNuovoResponsabile);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante la riassegnazione del progetto ID: " + idProgetto, e);
            throw e;
        }
    }

    public void creaNuovoProgetto(Progetto nuovoProgetto) throws SQLException {
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(INSERT_PROGETTO_QUERY)) {
            stmt.setString(1, nuovoProgetto.getNomeProgetto());
            stmt.setString(2, nuovoProgetto.getDescrizioneProgetto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante la creazione del progetto: " + nuovoProgetto.getNomeProgetto(), e);
            throw e; // Rilancia per notificare il controller
        }
    }

    public List<Progetto> getProgettiNonAssegnati() {
        List<Progetto> progetti = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(SELECT_PROGETTI_NON_ASSEGNATI_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Progetto progetto = new Progetto();
                progetto.setIdProgetto(rs.getInt("ID_Progetto"));
                progetto.setNomeProgetto(rs.getString("Nome_Progetto"));
                progetti.add(progetto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei progetti non assegnati", e);
        }
        return progetti;
    }
}
