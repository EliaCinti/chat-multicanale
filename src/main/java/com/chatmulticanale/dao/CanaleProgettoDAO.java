package com.chatmulticanale.dao;

import com.chatmulticanale.dao.costanti.CostantiCanaleProgettoDAO;
import com.chatmulticanale.dao.costanti.CostantiProgettoDAO;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CanaleProgettoDAO {

    private static final Logger logger = Logger.getLogger(CanaleProgettoDAO.class.getName());

    /**
     * Inserisce un nuovo canale nel database e aggiunge automaticamente il creatore come partecipante,
     * utilizzando la Stored Procedure sp_CP1_CreaCanaleProgetto.
     * La SP gestisce entrambe le operazioni in una transazione.
     *
     * @param nuovoCanale L'oggetto CanaleProgetto contenente nome e descrizione.
     * @param idProgetto L'ID del progetto a cui associare il canale.
     * @param idUtenteCreatore L'ID del Capo Progetto che sta creando il canale.
     * @throws SQLException se si verifica un errore durante l'esecuzione della Stored Procedure.
     */
    public void creaNuovoCanale(CanaleProgetto nuovoCanale, int idProgetto, int idUtenteCreatore) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire creaNuovoCanale perchè la connessione al database è assente.");
            return;
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiCanaleProgettoDAO.SP_CREA_CANALE)) {
            // I parametri sono: p_Nome_Canale, p_Descrizione_Canale, p_ID_Progetto, p_ID_Utente_Creatore
            stmt.setString(1, nuovoCanale.getNomeCanale());
            stmt.setString(2, nuovoCanale.getDescrizioneCanale());
            stmt.setInt(3, idProgetto);
            stmt.setInt(4, idUtenteCreatore);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile creare il canale '" + nuovoCanale.getNomeCanale() + "' per il progetto ID: " + idProgetto, e);
        }
    }

    /**
     * Recupera tutti i canali associati a un dato progetto.
     *
     * @param idProgetto L'ID del progetto di cui cercare i canali.
     * @return Una lista di oggetti CanaleProgetto (potenzialmente vuota).
     */
    public List<CanaleProgetto> getCanaliPerProgetto(int idProgetto) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire getCanaliPerProgetto perchè la connessione al database è assente.");
            return Collections.emptyList();
        }

        List<CanaleProgetto> canali = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(CostantiCanaleProgettoDAO.SELECT_CANALI_DI_PROGETTO)) {
            stmt.setInt(1, idProgetto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CanaleProgetto canale = new CanaleProgetto();
                    canale.setIdCanale(rs.getInt(CostantiCanaleProgettoDAO.ID_CANALE));
                    canale.setNomeCanale(rs.getString(CostantiCanaleProgettoDAO.NOME_CANALE));
                    canali.add(canale);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei canali per il progetto ID: " + idProgetto);        }
        return canali;
    }

    /**
     * Recupera la lista di tutti i canali a cui un utente partecipa.
     * Utilizza la Stored Procedure sp_UT6.
     *
     * @param idUtente L'ID dell'utente.
     * @return Una lista di oggetti CanaleProgetto.
     */
    public List<CanaleProgetto> getCanaliPartecipatiDaUtente(int idUtente) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire getCanaliPartecipatiDaUtente perchè la connessione al database è assente.");
            return Collections.emptyList();
        }

        List<CanaleProgetto> canali = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(CostantiCanaleProgettoDAO.SP_GET_CANALI_PARTECIPATI)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CanaleProgetto canale = new CanaleProgetto();
                    canale.setIdCanale(rs.getInt(CostantiCanaleProgettoDAO.ID_CANALE));
                    canale.setNomeCanale(rs.getString(CostantiCanaleProgettoDAO.NOME_CANALE));
                    canale.setDescrizioneCanale(rs.getString(CostantiCanaleProgettoDAO.DESCRIZIONE_CANALE));
                    canale.setIdProgetto(rs.getInt(CostantiProgettoDAO.ID_PROGETTO));
                    canali.add(canale);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei canali per l'utente ID: " + idUtente);
        }
        return canali;
    }
}
