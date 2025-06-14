package com.chatmulticanale.controller;

import com.chatmulticanale.dao.CanaleProgettoDAO;
import com.chatmulticanale.dao.ChatPrivataDAO;
import com.chatmulticanale.dao.MessaggioDAO;
import com.chatmulticanale.dto.ChatPrivataDTO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.model.CanaleProgetto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InterazioneUtenteController {

    private final MessaggioDAO messaggioDAO = new MessaggioDAO();
    private final CanaleProgettoDAO canaleDAO = new CanaleProgettoDAO();
    private final ChatPrivataDAO chatPrivataDAO = new ChatPrivataDAO();

    public static final int MESSAGGI_PER_PAGINA = 10;

    /**
     * Recupera la lista dei canali a cui un utente partecipa.
     *
     * @param idUtente L'ID dell'utente.
     * @return Una lista di CanaleProgetto.
     */
    public List<CanaleProgetto> getCanaliUtente(int idUtente) {
        return canaleDAO.getCanaliPartecipatiDaUtente(idUtente);
    }

    /**
     * Recupera una pagina di messaggi da un canale di progetto.
     *
     * @param idCanale L'ID del canale.
     * @param idVisualizzatore L'ID dell'utente che effettua la richiesta.
     * @param pagina Il numero di pagina desiderato.
     * @return Una lista di {@link MessaggioDTO}.
     */
    public List<MessaggioDTO> getPaginaMessaggiCanale(int idCanale, int idVisualizzatore, int pagina) {
        return messaggioDAO.getMessaggiCanalePaginati(idCanale, idVisualizzatore, MESSAGGI_PER_PAGINA, pagina);
    }

    /**
     * Gestisce la logica per inviare un nuovo messaggio in un canale di progetto.
     *
     * @param idCanale L'ID del canale di destinazione.
     * @param idMittente L'ID dell'utente che sta inviando.
     * @param contenuto Il testo del messaggio.
     * @return true se l'invio ha successo, false altrimenti.
     */
    public boolean inviaMessaggioCanale(int idCanale, int idMittente, String contenuto) {
        try {
            messaggioDAO.inviaMessaggioInCanale(idCanale, idMittente, contenuto);
            return true;
        } catch (SQLException e) {
            // L'errore è già stato loggato dal DAO.
            return false;
        }
    }

    /**
     * Gestisce la logica per inviare un messaggio con citazione in un canale.
     *
     * @param idCanale L'ID del canale.
     * @param idMittente L'ID del mittente.
     * @param contenuto Il testo del messaggio.
     * @param idMessaggioCitato L'ID del messaggio da citare.
     * @return true se l'invio ha successo, false altrimenti.
     */
    public boolean inviaMessaggioConCitazioneCanale(int idCanale, int idMittente, String contenuto, int idMessaggioCitato) {
        try {
            messaggioDAO.inviaMessaggioConCitazioneInCanale(idCanale, idMittente, contenuto, idMessaggioCitato);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean inviaMessaggioChat(int idChat, int idMittente, String contenuto) {
        try {
            messaggioDAO.inviaMessaggioInChat(idChat, idMittente, contenuto);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean inviaMessaggioConCitazioneChat(int idChat, int idMittente, String contenuto, int idMessaggioCitato) {
        try {
            messaggioDAO.inviaMessaggioConCitazioneInChat(idChat, idMittente, contenuto, idMessaggioCitato);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Gestisce la logica di business per avviare una nuova chat privata a partire da un messaggio.
     * Tenta di creare la chat tramite il DAO e restituisce un risultato che indica l'esito.
     *
     * @param idMessaggioOrigine L'ID del messaggio da cui avviare la conversazione.
     * @param idIniziatore L'ID dell'utente che sta compiendo l'azione.
     * @return Un {@code Optional<String>} che è:
     *         <ul>
     *           <li><b>vuoto</b> ({@code Optional.empty()}) se la chat è stata creata con successo.</li>
     *           <li><b>contenente</b> il messaggio di errore ({@code Optional.of(errorMessage)}) se
     *               la creazione è fallita (es. Chat con se stessi, messaggio non valido, ecc.).</li>
     *         </ul>
     */
    public Optional<String> avviaNuovaChatPrivata(int idMessaggioOrigine, int idIniziatore) {
        try {
            chatPrivataDAO.avviaChatPrivata(idMessaggioOrigine, idIniziatore);
            return Optional.empty(); // Successo: restituisce un Optional vuoto
        } catch (SQLException e) {
            // Errore: restituisce un Optional contenente il messaggio di errore proveniente dal DB/SP
            return Optional.of(e.getMessage());
        }
    }

    /**
     * Recupera una pagina di messaggi da una chat privata.
     *
     * @param idChat L'ID della chat.
     * @param idVisualizzatore L'ID dell'utente che effettua la richiesta.
     * @param pagina Il numero di pagina desiderato.
     * @return Una lista di {@link MessaggioDTO}.
     */
    public List<MessaggioDTO> getPaginaMessaggiChatPrivata(int idChat, int idVisualizzatore, int pagina) {
        return messaggioDAO.getMessaggiChatPrivataPaginati(idChat, idVisualizzatore, MESSAGGI_PER_PAGINA, pagina);
    }

    public List<ChatPrivataDTO> getMieChatPrivate(int idUtente) {
        return chatPrivataDAO.getChatDiUtente(idUtente);
    }
}