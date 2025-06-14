// src/main/java/com/chatmulticanale/dao/PartecipaCanaleDAO.java
package com.chatmulticanale.dao;

import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PartecipaCanaleDAO {

    private static final Logger logger = Logger.getLogger(PartecipaCanaleDAO.class.getName());

    // --- Stored Procedures ---
    private static final String SP_AGGIUNGI_UTENTE_A_CANALE = "{CALL sp_CP2_AggiungiUtenteACanale(?, ?)}";
    private static final String SP_RIMUOVI_UTENTE_DA_CANALE = "{CALL sp_CP3_RimuoviUtenteDaCanale(?, ?)}";

    /**
     * Aggiunge un utente a un canale specifico eseguendo la Stored Procedure sp_CP2.
     *
     * @param idCanale L'ID del canale.
     * @param idUtente L'ID dell'utente da aggiungere.
     * @throws SQLException se si verifica un errore durante l'operazione.
     */
    public void aggiungiUtenteACanale(int idCanale, int idUtente) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_AGGIUNGI_UTENTE_A_CANALE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'aggiunta dell'utente ID " + idUtente + " al canale ID " + idCanale, e);
            throw e;
        }
    }

    /**
     * Rimuove un utente da un canale specifico eseguendo la Stored Procedure sp_CP3.
     *
     * @param idCanale L'ID del canale.
     * @param idUtente L'ID dell'utente da rimuovere.
     * @throws SQLException se si verifica un errore durante l'operazione.
     */
    public void rimuoviUtenteDaCanale(int idCanale, int idUtente) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_RIMUOVI_UTENTE_DA_CANALE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante la rimozione dell'utente ID " + idUtente + " dal canale ID " + idCanale, e);
            throw e;
        }
    }
}