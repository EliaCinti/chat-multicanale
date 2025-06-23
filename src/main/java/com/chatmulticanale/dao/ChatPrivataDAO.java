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

/**
 * DAO per la gestione delle chat private nel database.
 * Offre metodi per supervisione delle chat di progetto, avvio di nuove chat private
 * e recupero delle chat dell'utente.
 * Utilizza Stored Procedures definite in {@link CostantiChatPrivataDAO}
 * e gestisce la connessione tramite {@link DatabaseConnector}.
 */
public class ChatPrivataDAO {

    private static final Logger logger = Logger.getLogger(ChatPrivataDAO.class.getName());

    /**
     * Recupera le chat private originate da un progetto, per supervisione da parte del capo progetto.
     * Utilizza la Stored Procedure {@code sp_CP4}.
     *
     * @param idProgetto      identificativo del progetto di origine delle chat
     * @param idCapoProgetto  identificativo del capo progetto richiedente (per autorizzazione)
     * @return lista di {@link ChatSupervisioneDTO} con informazioni di anteprima delle chat
     * @throws SQLException se la connessione è assente o si verifica un errore SQL (es. Permessi negati)
     * @see CostantiChatPrivataDAO#SP_GET_CHAT_DA_SUPERVISIONARE
     * @see DatabaseConnector#getConnection()
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
     * Avvia una nuova chat privata a partire da un messaggio esistente.
     * Utilizza la Stored Procedure {@code sp_UT3}.
     *
     * @param idMessaggioOrigine identificativo del messaggio di partenza
     * @param idUtenteIniziatore identificativo dell'utente che avvia la chat
     * @throws SQLException se la connessione è assente o si verifica un errore SQL (es. Chat non valida)
     * @see CostantiChatPrivataDAO#SP_AVVIA_CHAT_PRIVATA
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
     * Recupera tutte le chat private di cui un utente è partecipante.
     * Utilizza la Stored Procedure {@code sp_UT7}.
     *
     * @param idUtente identificativo dell'utente
     * @return lista di {@link ChatPrivataDTO} con le chat dell'utente
     *         (lista vuota in caso di errore di connessione o SQL)
     * @see CostantiChatPrivataDAO#SP_GET_CHAT_UTENTE
     */
    public List<ChatPrivataDTO> getChatDiUtente(int idUtente) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire getChatDiUtente perchè la connessione al database è assente.");
            return Collections.emptyList();
        }

        List<ChatPrivataDTO> chatList = new ArrayList<>();
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