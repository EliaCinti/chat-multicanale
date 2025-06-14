package com.chatmulticanale.dao;

import com.chatmulticanale.dto.ChatSupervisioneDTO;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatPrivataDAO {

    private static final Logger logger = Logger.getLogger(ChatPrivataDAO.class.getName());

    // --- Stored Procedures ---
    private static final String SP_GET_CHAT_DA_SUPERVISIONARE = "{CALL sp_CP4_AccediChatPrivateProgetto(?, ?)}";
    private static final String SP_AVVIA_CHAT_PRIVATA = "{CALL sp_UT3_AvviaChatPrivataDaMessaggio(?, ?)}";

    // --- Query Dirette ---


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
        List<ChatSupervisioneDTO> chatList = new ArrayList<>();
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_GET_CHAT_DA_SUPERVISIONARE)) {
            stmt.setInt(1, idCapoProgetto);
            stmt.setInt(2, idProgetto);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChatSupervisioneDTO dto = new ChatSupervisioneDTO();
                    dto.setIdChat(rs.getInt("ID_Chat"));
                    dto.setDataCreazioneChat(rs.getTimestamp("Timestamp_Creazione"));
                    dto.setCreatoreChatUsername(rs.getString("Creatore_Chat_Username"));
                    dto.setPartecipanteChatUsername(rs.getString("Partecipante_Chat_Username"));
                    dto.setCanaleOrigineNome(rs.getString("Canale_Origine_Nome"));

                    String contenuto = rs.getString("Messaggio_Originale_Contenuto");
                    // Tronchiamo il messaggio per un'anteprima pulita
                    if (contenuto != null && contenuto.length() > 50) {
                        contenuto = contenuto.substring(0, 47) + "...";
                    }
                    dto.setMessaggioOrigineContenuto(contenuto);

                    chatList.add(dto);
                }
            }
        } catch (SQLException e) {
            // La SP può lanciare un errore se l'utente non è il responsabile, lo logghiamo e lo rilanciamo
            logger.log(Level.WARNING, "Tentativo di accesso alle chat del progetto ID " + idProgetto + " da parte dell'utente ID " + idCapoProgetto + " fallito: " + e.getMessage());
            throw e;
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
        try (CallableStatement stmt = DatabaseConnector.getConnection().prepareCall(SP_AVVIA_CHAT_PRIVATA)) {
            stmt.setInt(1, idUtenteIniziatore);
            stmt.setInt(2, idMessaggioOrigine);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Tentativo di avviare una chat dal messaggio ID " + idMessaggioOrigine + " da parte dell'utente ID " + idUtenteIniziatore + " fallito: " + e.getMessage());
            throw e;
        }
    }
}