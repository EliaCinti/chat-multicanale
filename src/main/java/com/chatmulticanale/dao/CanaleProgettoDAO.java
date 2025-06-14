// src/main/java/com/chatmulticanale/dao/CanaleProgettoDAO.java
package com.chatmulticanale.dao;

import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CanaleProgettoDAO {

    private static final Logger logger = Logger.getLogger(CanaleProgettoDAO.class.getName());

    // --- Stored Procedures ---
    private static final String SP_CREA_CANALE = "{CALL sp_CP1_CreaCanaleProgetto(?, ?, ?, ?)}";

    // --- Query Dirette ---
    private static final String SELECT_CANALI_DI_PROGETTO = "SELECT ID_Canale, Nome_Canale FROM CanaleProgetto WHERE Progetto = ?";
    private static final String SELECT_CANALI_PARTECIPATI_DA_UTENTE =
            "SELECT cp.ID_Canale, cp.Nome_Canale, cp.Descrizione_Canale " +
                    "FROM CanaleProgetto cp JOIN PartecipaCanale pc ON cp.ID_Canale = pc.ID_Canale " +
                    "WHERE pc.ID_Utente = ?";

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
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_CREA_CANALE)) {
            // I parametri sono: p_Nome_Canale, p_Descrizione_Canale, p_ID_Progetto, p_ID_Utente_Creatore
            stmt.setString(1, nuovoCanale.getNomeCanale());
            stmt.setString(2, nuovoCanale.getDescrizioneCanale());
            stmt.setInt(3, idProgetto);
            stmt.setInt(4, idUtenteCreatore);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante la creazione del canale '" + nuovoCanale.getNomeCanale() + "' per il progetto ID: " + idProgetto, e);
            throw e; // Rilancia l'eccezione per notificare il controller
        }
    }

    /**
     * Recupera tutti i canali associati a un dato progetto.
     *
     * @param idProgetto L'ID del progetto di cui cercare i canali.
     * @return Una lista di oggetti CanaleProgetto (potenzialmente vuota).
     */
    public List<CanaleProgetto> getCanaliPerProgetto(int idProgetto) {
        List<CanaleProgetto> canali = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(SELECT_CANALI_DI_PROGETTO)) {
            stmt.setInt(1, idProgetto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CanaleProgetto canale = new CanaleProgetto();
                    canale.setIdCanale(rs.getInt("ID_Canale"));
                    canale.setNomeCanale(rs.getString("Nome_Canale"));
                    canali.add(canale);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei canali per il progetto ID: " + idProgetto, e);
        }
        return canali;
    }

    /**
     * Recupera la lista di tutti i canali a cui un utente partecipa.
     *
     * @param idUtente L'ID dell'utente.
     * @return Una lista di oggetti CanaleProgetto.
     */
    public List<CanaleProgetto> getCanaliPartecipatiDaUtente(int idUtente) {
        List<CanaleProgetto> canali = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnector.getConnection().prepareStatement(SELECT_CANALI_PARTECIPATI_DA_UTENTE)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CanaleProgetto canale = new CanaleProgetto();
                    canale.setIdCanale(rs.getInt("ID_Canale"));
                    canale.setNomeCanale(rs.getString("Nome_Canale"));
                    canale.setDescrizioneCanale(rs.getString("Descrizione_Canale"));
                    canali.add(canale);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei canali per l'utente ID: " + idUtente, e);
        }
        return canali;
    }

}