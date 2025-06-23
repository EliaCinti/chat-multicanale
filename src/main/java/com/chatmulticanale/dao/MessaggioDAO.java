package com.chatmulticanale.dao;

import com.chatmulticanale.dao.costanti.CostantiMessaggioDAO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessaggioDAO {

    private static final Logger logger = Logger.getLogger(MessaggioDAO.class.getName());

    /**
     * Recupera una pagina di messaggi da un canale di progetto.
     * <p>
     * I messaggi vengono arricchiti con le informazioni del mittente tramite la Stored
     * Procedure {@code sp_UT2_VisualizzaMessaggiCanale}. In caso di errore,
     * logga e restituisce una lista vuota.
     *
     * @param idCanale L'ID del canale.
     * @param dimensionePagina Il numero di messaggi per pagina.
     * @param numeroPagina Il numero della pagina (partendo da 1).
     * @return Una {@code List<MessaggioDTO>} contenente i messaggi della pagina richiesta.
     */
    public List<MessaggioDTO> getMessaggiCanalePaginati(int idCanale, int idVisualizzatore, int dimensionePagina, int numeroPagina) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getMessaggiCanalePaginati: connessione al database assente.");
            return Collections.emptyList();
        }

        List<MessaggioDTO> messaggi = new ArrayList<>();

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_GET_MESSAGGI_CANALE)) {
            // Impostiamo i parametri nell'ordine corretto
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idVisualizzatore);
            stmt.setInt(3, numeroPagina);
            stmt.setInt(4, dimensionePagina);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MessaggioDTO dto = new MessaggioDTO();
                    dto.setIdMessaggio(rs.getInt(CostantiMessaggioDAO.ID_MESSAGGIO));
                    dto.setContenuto(rs.getString(CostantiMessaggioDAO.CONTENUTO));
                    dto.setTimestamp(rs.getTimestamp(CostantiMessaggioDAO.TIMESTAMP));
                    dto.setUsernameMittente(rs.getString(CostantiMessaggioDAO.MITTENTE_USERNAME));
                    messaggi.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei messaggi per il canale ID: " + idCanale);        }
        return messaggi;
    }

    /**
     * Recupera una pagina di messaggi da una chat privata.
     * <p>
     * I messaggi vengono arricchiti con le informazioni del mittente tramite la Stored
     * Procedure {@code sp_UT5_VisualizzaMessaggiChatPrivata}. In caso di errore,
     * logga e restituisce una lista vuota.
     *
     * @param idChat L'ID della chat privata.
     * @param dimensionePagina Il numero di messaggi per pagina.
     * @param numeroPagina Il numero della pagina (partendo da 1).
     * @return Una {@code List<MessaggioDTO>} contenente i messaggi della pagina richiesta.
     */
    public List<MessaggioDTO> getMessaggiChatPrivataPaginati(int idChat, int idVisualizzatore, int dimensionePagina, int numeroPagina) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getMessaggiChatPrivataPaginati: connessione al database assente.");
            return Collections.emptyList();
        }

        List<MessaggioDTO> messaggi = new ArrayList<>();

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_GET_MESSAGGI_CHAT)) {
            // Impostiamo i parametri nell'ordine corretto
            stmt.setInt(1, idChat);
            stmt.setInt(2, idVisualizzatore);
            stmt.setInt(3, numeroPagina);
            stmt.setInt(4, dimensionePagina);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MessaggioDTO dto = new MessaggioDTO();
                    dto.setIdMessaggio(rs.getInt(CostantiMessaggioDAO.ID_MESSAGGIO));
                    dto.setContenuto(rs.getString(CostantiMessaggioDAO.CONTENUTO));
                    dto.setTimestamp(rs.getTimestamp(CostantiMessaggioDAO.TIMESTAMP));
                    dto.setUsernameMittente(rs.getString(CostantiMessaggioDAO.MITTENTE_USERNAME));
                    messaggi.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei messaggi per la chat privata ID: " + idChat);        }
        return messaggi;
    }

    /**
     * Inserisce un nuovo messaggio semplice (senza citazione) in un canale di progetto.
     * <p>
     * Utilizza la Stored Procedure {@code sp_UT1a_InviaMessaggioCanaleSemplice}.
     *
     * @param idCanale L'ID del canale in cui inviare il messaggio.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
     */
    public void inviaMessaggioInCanale(int idCanale, int idMittente, String contenuto) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire inviaMessaggioInCanale: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_INVIA_MESSAGGIO_CANALE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile inviare messaggio al canale ID: " + idCanale, e);
        }
    }

    /**
     * Inserisce un nuovo messaggio con citazione in un canale di progetto.
     * <p>
     * Utilizza la Stored Procedure {@code sp_UT1b_InviaMessaggioCanaleConCitazione}.
     *
     * @param idCanale L'ID del canale.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @param idMessaggioCitato L'ID del messaggio a cui si sta rispondendo.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
     */
    public void inviaMessaggioConCitazioneInCanale(int idCanale, int idMittente, String contenuto, int idMessaggioCitato) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire inviaMessaggioConCitazioneInCanale: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_INVIA_MESSAGGIO_CANALE_CITAZIONE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);
            stmt.setInt(4, idMessaggioCitato);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile inviare messaggio con citazione al canale ID: " + idCanale, e);
        }
    }

    /**
     * Inserisce un nuovo messaggio semplice in una chat privata.
     * <p>
     * Utilizza la Stored Procedure {@code sp_UT4a_InviaMessaggioChatPrivataSemplice}.
     *
     * @param idChat L'ID della chat privata.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
     */
    public void inviaMessaggioInChat(int idChat, int idMittente, String contenuto) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire inviaMessaggioInChat: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_INVIA_MESSAGGIO_CHAT)) {
            stmt.setInt(1, idChat);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile inviare messaggio alla chat ID: " + idChat, e);
        }
    }

    /**
     * Inserisce un nuovo messaggio con citazione in una chat privata.
     * <p>
     * Utilizza la Stored Procedure {@code sp_UT4b_InviaMessaggioChatPrivataConCitazione}.
     *
     * @param idChat L'ID della chat privata.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @param idMessaggioCitato L'ID del messaggio da citare.
     * @throws SQLException se la connessione non è disponibile o se si verifica un errore del database.
     */
    public void inviaMessaggioConCitazioneInChat(int idChat, int idMittente, String contenuto, int idMessaggioCitato) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            throw new SQLException("Impossibile eseguire inviaMessaggioConCitazioneInChat: connessione al database assente.");
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_INVIA_MESSAGGIO_CHAT_CITAZIONE)) {
            stmt.setInt(1, idChat);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);
            stmt.setInt(4, idMessaggioCitato);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile inviare messaggio con citazione alla chat ID: " + idChat, e);
        }
    }
}
