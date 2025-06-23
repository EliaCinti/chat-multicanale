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

/**
 * Controller che gestisce l'interazione dell'utente con canali di progetto e chat private.
 * Fornisce metodi per recuperare canali, inviare e paginare messaggi, e avviare nuove chat private.
 */
public class InterazioneUtenteController {

    private final MessaggioDAO messaggioDAO = new MessaggioDAO();
    private final CanaleProgettoDAO canaleDAO = new CanaleProgettoDAO();
    private final ChatPrivataDAO chatPrivataDAO = new ChatPrivataDAO();

    /** Numero di messaggi restituiti per pagina nelle operazioni di paginazione */
    public static final int MESSAGGI_PER_PAGINA = 10;

    /**
     * Recupera la lista dei canali a cui un utente partecipa.
     *
     * @param idUtente identificativo dell'utente
     * @return lista di {@link CanaleProgetto} partecipati dall'utente
     * @see CanaleProgettoDAO#getCanaliPartecipatiDaUtente(int)
     */
    public List<CanaleProgetto> getCanaliUtente(int idUtente) {
        return canaleDAO.getCanaliPartecipatiDaUtente(idUtente);
    }

    /**
     * Recupera una pagina di messaggi da un canale di progetto.
     *
     * @param idCanale      identificativo del canale
     * @param idVisualizzatore identificativo dell'utente che richiede i messaggi
     * @param pagina        numero della pagina di messaggi desiderata (0-based)
     * @return lista di pagine di {@link MessaggioDTO} contenenti i messaggi
     * @see MessaggioDAO#getMessaggiCanalePaginati(int, int, int, int)
     */
    public List<MessaggioDTO> getPaginaMessaggiCanale(int idCanale, int idVisualizzatore, int pagina) {
        return messaggioDAO.getMessaggiCanalePaginati(idCanale, idVisualizzatore, MESSAGGI_PER_PAGINA, pagina);
    }

    /**
     * Invia un nuovo messaggio in un canale di progetto.
     *
     * @param idCanale identificativo del canale di destinazione
     * @param idMittente identificativo dell'utente mittente
     * @param contenuto  testo del messaggio
     * @return {@code true} se l'invio ha successo, {@code false} in caso di errore SQL
     * @see MessaggioDAO#inviaMessaggioInCanale(int, int, String)
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
     * Invia un messaggio con citazione in un canale di progetto.
     *
     * @param idCanale          identificativo del canale
     * @param idMittente        identificativo dell'utente mittente
     * @param contenuto         testo del messaggio
     * @param idMessaggioCitato identificativo del messaggio citato
     * @return {@code true} se l'invio ha successo, {@code false} in caso di errore SQL
     * @see MessaggioDAO#inviaMessaggioConCitazioneInCanale(int, int, String, int)
     */
    public boolean inviaMessaggioConCitazioneCanale(int idCanale, int idMittente, String contenuto, int idMessaggioCitato) {
        try {
            messaggioDAO.inviaMessaggioConCitazioneInCanale(idCanale, idMittente, contenuto, idMessaggioCitato);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Invia un messaggio semplice in una chat privata.
     *
     * @param idChat     identificativo della chat privata
     * @param idMittente identificativo dell'utente mittente
     * @param contenuto  testo del messaggio
     * @return {@code true} se l'invio ha successo, {@code false} in caso di errore SQL
     * @see MessaggioDAO#inviaMessaggioInChat(int, int, String)
     */
    public boolean inviaMessaggioChat(int idChat, int idMittente, String contenuto) {
        try {
            messaggioDAO.inviaMessaggioInChat(idChat, idMittente, contenuto);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Invia un messaggio con citazione in una chat privata.
     *
     * @param idChat           identificativo della chat privata
     * @param idMittente       identificativo dell'utente mittente
     * @param contenuto        testo del messaggio
     * @param idMessaggioCitato identificativo del messaggio citato
     * @return {@code true} se l'invio ha successo, {@code false} in caso di errore SQL
     * @see MessaggioDAO#inviaMessaggioConCitazioneInChat(int, int, String, int)
     */
    public boolean inviaMessaggioConCitazioneChat(int idChat, int idMittente, String contenuto, int idMessaggioCitato) {
        try {
            messaggioDAO.inviaMessaggioConCitazioneInChat(idChat, idMittente, contenuto, idMessaggioCitato);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Avvia una nuova chat privata a partire da un messaggio esistente.
     *
     * @param idMessaggioOrigine identificativo del messaggio di partenza
     * @param idIniziatore       identificativo dell'utente che avvia la chat
     * @return {@code Optional.empty()} se la chat è creata con successo;
     *         {@code Optional.of(errorMessage)} contenente il messaggio di errore in caso di fallimento
     * @see ChatPrivataDAO#avviaChatPrivata(int, int)
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
     * @param idChat         identificativo della chat privata
     * @param idVisualizzatore identificativo dell'utente che richiede i messaggi
     * @param pagina         numero della pagina desiderata (0-based)
     * @return lista di {@link MessaggioDTO} contenenti i messaggi della pagina
     * @see MessaggioDAO#getMessaggiChatPrivataPaginati(int, int, int, int)
     */
    public List<MessaggioDTO> getPaginaMessaggiChatPrivata(int idChat, int idVisualizzatore, int pagina) {
        return messaggioDAO.getMessaggiChatPrivataPaginati(idChat, idVisualizzatore, MESSAGGI_PER_PAGINA, pagina);
    }

    /**
     * Recupera le chat private di cui un utente è partecipante.
     *
     * @param idUtente identificativo dell'utente loggato
     * @return lista di {@link ChatPrivataDTO} rappresentanti le chat dell'utente
     * @see ChatPrivataDAO#getChatDiUtente(int)
     */
    public List<ChatPrivataDTO> getMieChatPrivate(int idUtente) {
        return chatPrivataDAO.getChatDiUtente(idUtente);
    }
}