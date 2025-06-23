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

/**
 * DAO per la gestione dei messaggi in canali di progetto e chat private.
 * Utilizza Stored Procedures definite in {@link CostantiMessaggioDAO}
 * e connessioni fornite da {@link DatabaseConnector}.
 * Fornisce metodi per recuperare messaggi con paginazione e inviare messaggi,
 * sia semplici che con citazione, in canali e chat.
 */
public class MessaggioDAO {

    private static final Logger logger = Logger.getLogger(MessaggioDAO.class.getName());

    /**
     * Recupera una pagina di messaggi da un canale di progetto.
     * Utilizza la Stored Procedure {@code sp_UT2_VisualizzaMessaggiCanale}.
     * In caso di errore, logga e restituisce lista vuota.
     *
     * @param idCanale       identificativo del canale di progetto
     * @param idVisualizzatore identificativo dell'utente richiedente (per permessi)
     * @param dimensionePagina numero di messaggi per pagina
     * @param numeroPagina    numero della pagina (1-based)
     * @return lista di {@link MessaggioDTO} con i messaggi richiesti; lista vuota in caso di errore
     * @see CostantiMessaggioDAO#SP_GET_MESSAGGI_CANALE
     * @see DatabaseConnector#getConnection()
     */
    public List<MessaggioDTO> getMessaggiCanalePaginati(int idCanale, int idVisualizzatore, int dimensionePagina, int numeroPagina) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getMessaggiCanalePaginati: connessione al database assente.");
            return Collections.emptyList();
        }

        List<MessaggioDTO> messaggi = new ArrayList<>();

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_GET_MESSAGGI_CANALE)) {
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
                    int citato = rs.getInt(CostantiMessaggioDAO.MESSAGGIO_CITATO);
                    if (rs.wasNull()) {
                        dto.setIdMessaggioCitato(null);
                    } else {
                        dto.setIdMessaggioCitato(citato);
                    }
                    messaggi.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei messaggi per il canale ID: " + idCanale);        }
        return messaggi;
    }

    /**
     * Recupera una pagina di messaggi da una chat privata.
     * Utilizza la Stored Procedure {@code sp_UT5_VisualizzaMessaggiChatPrivata}.
     * In caso di errore, logga e restituisce lista vuota.
     *
     * @param idChat           identificativo della chat privata
     * @param idVisualizzatore identificativo dell'utente richiedente (per permessi)
     * @param dimensionePagina numero di messaggi per pagina
     * @param numeroPagina     numero della pagina (1-based)
     * @return lista di {@link MessaggioDTO} con i messaggi richiesti; lista vuota in caso di errore
     * @see CostantiMessaggioDAO#SP_GET_MESSAGGI_CHAT
     * @see DatabaseConnector#getConnection()
     */
    public List<MessaggioDTO> getMessaggiChatPrivataPaginati(int idChat, int idVisualizzatore, int dimensionePagina, int numeroPagina) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, () -> "Impossibile eseguire getMessaggiChatPrivataPaginati: connessione al database assente.");
            return Collections.emptyList();
        }

        List<MessaggioDTO> messaggi = new ArrayList<>();

        try (CallableStatement stmt = conn.prepareCall(CostantiMessaggioDAO.SP_GET_MESSAGGI_CHAT)) {
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
                    int citato = rs.getInt(CostantiMessaggioDAO.MESSAGGIO_CITATO);
                    if (rs.wasNull()) {
                        dto.setIdMessaggioCitato(null);
                    } else {
                        dto.setIdMessaggioCitato(citato);
                    }
                    messaggi.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei messaggi per la chat privata ID: " + idChat);        }
        return messaggi;
    }

    /**
     * Invia un nuovo messaggio semplice (senza citazione) in un canale di progetto.
     * Utilizza la Stored Procedure {@code sp_UT1a_InviaMessaggioCanaleSemplice}.
     *
     * @param idCanale identificativo del canale
     * @param idMittente identificativo dell'utente che invia il messaggio
     * @param contenuto testo del messaggio
     * @throws SQLException se la connessione è assente o si verifica un errore SQL
     * @see CostantiMessaggioDAO#SP_INVIA_MESSAGGIO_CANALE
     * @see DatabaseConnector#getConnection()
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
     * Invia un nuovo messaggio con citazione in un canale di progetto.
     * Utilizza la Stored Procedure {@code sp_UT1b_InviaMessaggioCanaleConCitazione}.
     *
     * @param idCanale           identificativo del canale
     * @param idMittente         identificativo dell'utente che invia il messaggio
     * @param contenuto          testo del messaggio
     * @param idMessaggioCitato  identificativo del messaggio citato
     * @throws SQLException se la connessione è assente o si verifica un errore SQL
     * @see CostantiMessaggioDAO#SP_INVIA_MESSAGGIO_CANALE_CITAZIONE
     * @see DatabaseConnector#getConnection()
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
     * Invia un nuovo messaggio semplice in una chat privata.
     * Utilizza la Stored Procedure {@code sp_UT4a_InviaMessaggioChatPrivataSemplice}.
     *
     * @param idChat      identificativo della chat privata
     * @param idMittente  identificativo dell'utente che invia il messaggio
     * @param contenuto   testo del messaggio
     * @throws SQLException se la connessione è assente o si verifica un errore SQL
     * @see CostantiMessaggioDAO#SP_INVIA_MESSAGGIO_CHAT
     * @see DatabaseConnector#getConnection()
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
     * Invia un nuovo messaggio con citazione in una chat privata.
     * Utilizza la Stored Procedure {@code sp_UT4b_InviaMessaggioChatPrivataConCitazione}.
     *
     * @param idChat             identificativo della chat privata
     * @param idMittente         identificativo dell'utente che invia il messaggio
     * @param contenuto          testo del messaggio
     * @param idMessaggioCitato  identificativo del messaggio citato
     * @throws SQLException se la connessione è assente o si verifica un errore SQL
     * @see CostantiMessaggioDAO#SP_INVIA_MESSAGGIO_CHAT_CITAZIONE
     * @see DatabaseConnector#getConnection()
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
