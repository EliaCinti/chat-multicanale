package com.chatmulticanale.dao;

import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessaggioDAO {

    private static final Logger logger = Logger.getLogger(MessaggioDAO.class.getName());

    // --- Stored Procedures ---
    private static final String SP_GET_MESSAGGI_CANALE = "{CALL sp_UT2_VisualizzaMessaggiCanale(?, ?, ?, ?)}";
    private static final String SP_INVIA_MESSAGGIO_CANALE = "{CALL sp_UT1a_InviaMessaggioCanaleSemplice(?, ?, ?)}";
    private static final String SP_INVIA_MESSAGGIO_CANALE_CITAZIONE = "{CALL sp_UT1b_InviaMessaggioCanaleConCitazione(?, ?, ?, ?)}";
    private static final String SP_INVIA_MESSAGGIO_CHAT = "{CALL sp_UT4a_InviaMessaggioChatPrivataSemplice(?, ?, ?)}";
    private static final String SP_INVIA_MESSAGGIO_CHAT_CITAZIONE = "{CALL sp_UT4b_InviaMessaggioChatPrivataConCitazione(?, ?, ?, ?)}";
    private static final String SP_GET_MESSAGGI_CHAT = "{CALL sp_UT5_VisualizzaMessaggiChatPrivata(?, ?, ?, ?)}";

    /**
     * Recupera una pagina di messaggi da un canale di progetto, arricchiti con
     * le informazioni del mittente, utilizzando la Stored Procedure sp_UT2.
     *
     * @param idCanale L'ID del canale.
     * @param idVisualizzatore L'ID dell'utente che sta visualizzando i messaggi.
     * @param dimensionePagina Il numero di messaggi per pagina.
     * @param numeroPagina Il numero della pagina (partendo da 1).
     * @return Una lista di {@link MessaggioDTO}.
     */
    public List<MessaggioDTO> getMessaggiCanalePaginati(int idCanale, int idVisualizzatore, int dimensionePagina, int numeroPagina) {
        List<MessaggioDTO> messaggi = new ArrayList<>();

        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_GET_MESSAGGI_CANALE)) {
            // Impostiamo i parametri nell'ordine corretto
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idVisualizzatore);
            stmt.setInt(3, numeroPagina);
            stmt.setInt(4, dimensionePagina);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MessaggioDTO dto = new MessaggioDTO();
                    dto.setIdMessaggio(rs.getInt("ID_Messaggio"));
                    dto.setContenuto(rs.getString("Contenuto"));
                    dto.setTimestamp(rs.getTimestamp("Timestamp"));
                    dto.setUsernameMittente(rs.getString("Mittente_Username"));
                    messaggi.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei messaggi per il canale ID: " + idCanale, e);
        }
        return messaggi;
    }

    /**
     * Recupera una pagina di messaggi da una chat privata, arricchiti con
     * le informazioni del mittente, utilizzando la Stored Procedure sp_UT5.
     *
     * @param idChat L'ID della chat privata.
     * @param idVisualizzatore L'ID dell'utente che sta visualizzando i messaggi.
     * @param dimensionePagina Il numero di messaggi per pagina.
     * @param numeroPagina Il numero della pagina (partendo da 1).
     * @return Una lista di {@link MessaggioDTO}.
     */
    public List<MessaggioDTO> getMessaggiChatPrivataPaginati(int idChat, int idVisualizzatore, int dimensionePagina, int numeroPagina) {
        List<MessaggioDTO> messaggi = new ArrayList<>();

        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_GET_MESSAGGI_CHAT)) {
            // Impostiamo i parametri nell'ordine corretto
            stmt.setInt(1, idChat);
            stmt.setInt(2, idVisualizzatore);
            stmt.setInt(3, numeroPagina);
            stmt.setInt(4, dimensionePagina);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MessaggioDTO dto = new MessaggioDTO();
                    dto.setIdMessaggio(rs.getInt("ID_Messaggio"));
                    dto.setContenuto(rs.getString("Contenuto"));
                    dto.setTimestamp(rs.getTimestamp("Timestamp"));
                    dto.setUsernameMittente(rs.getString("Mittente_Username"));
                    messaggi.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero dei messaggi per la chat privata ID: " + idChat, e);
        }
        return messaggi;
    }

    /**
     * Inserisce un nuovo messaggio semplice (senza citazione) in un canale di progetto.
     * Utilizza la Stored Procedure sp_UT1a.
     *
     * @param idCanale L'ID del canale in cui inviare il messaggio.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @throws SQLException se si verifica un errore durante l'inserimento.
     */
    public void inviaMessaggioInCanale(int idCanale, int idMittente, String contenuto) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_INVIA_MESSAGGIO_CANALE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'invio del messaggio al canale ID: " + idCanale, e);
            throw e;
        }
    }

    /**
     * Inserisce un nuovo messaggio con citazione in un canale di progetto.
     * Utilizza la Stored Procedure sp_UT1b.
     *
     * @param idCanale L'ID del canale.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @param idMessaggioCitato L'ID del messaggio a cui si sta rispondendo.
     * @throws SQLException se si verifica un errore.
     */
    public void inviaMessaggioConCitazioneInCanale(int idCanale, int idMittente, String contenuto, int idMessaggioCitato) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_INVIA_MESSAGGIO_CANALE_CITAZIONE)) {
            stmt.setInt(1, idCanale);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);
            stmt.setInt(4, idMessaggioCitato);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'invio del messaggio con citazione al canale ID: " + idCanale, e);
            throw e;
        }
    }

    /**
     * Inserisce un nuovo messaggio semplice in una chat privata.
     * Utilizza la Stored Procedure sp_UT4a.
     *
     * @param idChat L'ID della chat privata.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @throws SQLException se si verifica un errore.
     */
    public void inviaMessaggioInChat(int idChat, int idMittente, String contenuto) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_INVIA_MESSAGGIO_CHAT)) {
            stmt.setInt(1, idChat);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'invio del messaggio alla chat ID: " + idChat, e);
            throw e;
        }
    }

    /**
     * Inserisce un nuovo messaggio con citazione in una chat privata.
     * Utilizza la Stored Procedure sp_UT4b.
     *
     * @param idChat L'ID della chat privata.
     * @param idMittente L'ID dell'utente che invia il messaggio.
     * @param contenuto Il testo del messaggio.
     * @param idMessaggioCitato L'ID del messaggio da citare.
     * @throws SQLException se si verifica un errore.
     */
    public void inviaMessaggioConCitazioneInChat(int idChat, int idMittente, String contenuto, int idMessaggioCitato) throws SQLException {
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_INVIA_MESSAGGIO_CHAT_CITAZIONE)) {
            stmt.setInt(1, idChat);
            stmt.setInt(2, idMittente);
            stmt.setString(3, contenuto);
            stmt.setInt(4, idMessaggioCitato);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante l'invio del messaggio con citazione alla chat ID: " + idChat, e);
            throw e;
        }
    }
}
