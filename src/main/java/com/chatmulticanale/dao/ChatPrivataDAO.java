package com.chatmulticanale.dao;

import com.chatmulticanale.dao.costanti.CostantiChatPrivataDAO;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.ChatSupervisioneDTO;
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

public class ChatPrivataDAO {

    private static final Logger logger = Logger.getLogger(ChatPrivataDAO.class.getName());

    /**
     * Recupera una lista di chat private originate da un progetto specifico,
     * per la supervisione da parte del Capo Progetto.
     * Utilizza la Stored Procedure sp_CP4.
     *
     * @param idProgetto L'ID del progetto da cui sono originate le chat.
     * @param idCapoProgetto L'ID del capo progetto che richiede l'accesso (per validazione).
     * @return Una lista di {@link ChatSupervisioneDTO}.
     * @throws SQLException se l'utente non è autorizzato o per altri errori DB.
     */
    public List<ChatSupervisioneDTO> getChatDaSupervisionare(int idProgetto, int idCapoProgetto) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire getChatDaSupervisionare perchè la connessione al database è assente.");
            return Collections.emptyList();
        }

        List<ChatSupervisioneDTO> chatList = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(CostantiChatPrivataDAO.SP_GET_CHAT_DA_SUPERVISIONARE)) {
            stmt.setInt(1, idCapoProgetto);
            stmt.setInt(2, idProgetto);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChatSupervisioneDTO dto = new ChatSupervisioneDTO();
                    dto.setIdChat(rs.getInt(CostantiChatPrivataDAO.ID_CHAT));
                    dto.setDataCreazioneChat(rs.getTimestamp(CostantiChatPrivataDAO.TIMESTAMP_CREAZIONE));
                    dto.setCreatoreChatUsername(rs.getString(CostantiChatPrivataDAO.CREATORE_CHAT));
                    dto.setPartecipanteChatUsername(rs.getString(CostantiChatPrivataDAO.PARTECIPANTE_CHAT));
                    dto.setCanaleOrigineNome(rs.getString(CostantiChatPrivataDAO.NOME_CANALE_ORIGINE));

                    String contenuto = rs.getString(CostantiChatPrivataDAO.MESSAGGIO_ORIGINE);
                    // Tronchiamo il messaggio per un'anteprima pulita
                    if (contenuto != null && contenuto.length() > 50) {
                        contenuto = contenuto.substring(0, 47) + "...";
                    }
                    dto.setMessaggioOrigineContenuto(contenuto);

                    chatList.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Impossibile recuperare le chat per la supervisione del progetto ID: " + idProgetto, e);
        }
        return chatList;
    }

    /**
     * Avvia una nuova chat privata a partire da un messaggio esistente in un canale.
     * Utilizza la Stored Procedure sp_UT3.
     *
     * @param idMessaggioOrigine L'ID del messaggio da cui si origina la chat.
     * @param idUtenteIniziatore L'ID dell'utente che sta avviando la chat.
     * @throws SQLException se si verifica un errore, ad esempio se il messaggio
     *         non è valido o se si tenta di avviare una chat con se stessi.
     */
    public void avviaChatPrivata(int idMessaggioOrigine, int idUtenteIniziatore) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire avviaChatPrivata perchè la connessione al database è assente.");
            return;
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiChatPrivataDAO.SP_AVVIA_CHAT_PRIVATA)) {
            stmt.setInt(1, idUtenteIniziatore);
            stmt.setInt(2, idMessaggioOrigine);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile avviare la chat privata dal messaggio ID: " + idMessaggioOrigine, e);
        }
    }

    /**
     * Recupera la lista di tutte le chat private in cui un utente è coinvolto.
     * Utilizza la Stored Procedure sp_UT7.
     *
     * @param idUtente L'ID dell'utente.
     * @return Una lista di {@link ChatPrivataDTO}.
     */
    public List<ChatPrivataDTO> getChatDiUtente(int idUtente) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire getChatDiUtente perchè la connessione al database è assente.");
            return Collections.emptyList();
        }

        List<ChatPrivataDTO> chatList = new ArrayList<>();
        // Usiamo la nuova SP
        try (CallableStatement stmt = conn.prepareCall(CostantiChatPrivataDAO.SP_GET_CHAT_UTENTE)) {
            stmt.setInt(1, idUtente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChatPrivataDTO dto = new ChatPrivataDTO();
                    dto.setIdChat(rs.getInt(CostantiChatPrivataDAO.ID_CHAT));
                    dto.setDataCreazione(rs.getTimestamp(CostantiChatPrivataDAO.TIMESTAMP_CREAZIONE));
                    // La SP ci dà già direttamente il nome dell'altro partecipante tramite alias (AS)
                    dto.setAltroPartecipanteUsername(rs.getString(CostantiChatPrivataDAO.ALTRO_PARTECIPANTE_USERNAME));
                    chatList.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero delle chat per l'utente ID: " + idUtente);        }
        return chatList;
    }
}