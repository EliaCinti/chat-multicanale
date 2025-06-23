package com.chatmulticanale.dao;

import com.chatmulticanale.dao.costanti.CostantiPartecipaCanaleDAO;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DAO responsabile della gestione delle partecipazioni degli utenti nei canali di progetto.
 * Offre metodi per aggiungere e rimuovere utenti da un canale, utilizzando le Stored Procedures
 * definite in {@link CostantiPartecipaCanaleDAO} e connessioni fornite da {@link DatabaseConnector}.
 */
public class PartecipaCanaleDAO {

    /**
     * Aggiunge un utente a un canale specifico.
     *
     * @param idCanale identificativo del canale di progetto
     * @param idUtente identificativo dell'utente da aggiungere
     * @throws SQLException se la connessione al database non è disponibile o se si verifica un errore SQL
     * @see CostantiPartecipaCanaleDAO#SP_AGGIUNGI_UTENTE_A_CANALE
     * @see DatabaseConnector#getConnection()
     */
    public void aggiungiUtenteACanale(int idCanale, int idUtente) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire aggiungiUtenteACanale: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiPartecipaCanaleDAO.SP_AGGIUNGI_UTENTE_A_CANALE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile aggiungere l'utente ID " + idUtente + " al canale ID " + idCanale, e);        }
    }

    /**
     * Rimuove un utente da un canale specifico.
     *
     * @param idCanale identificativo del canale di progetto
     * @param idUtente identificativo dell'utente da rimuovere
     * @throws SQLException se la connessione al database non è disponibile o se si verifica un errore SQL
     * @see CostantiPartecipaCanaleDAO#SP_RIMUOVI_UTENTE_DA_CANALE
     * @see DatabaseConnector#getConnection()
     */
    public void rimuoviUtenteDaCanale(int idCanale, int idUtente) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire rimuoviUtenteDaCanale: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiPartecipaCanaleDAO.SP_RIMUOVI_UTENTE_DA_CANALE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idUtente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile rimuovere l'utente ID " + idUtente + " dal canale ID " + idCanale, e);        }
    }
}