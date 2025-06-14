// src/main/java/com/chatmulticanale/controller/InterazioneUtenteController.java
package com.chatmulticanale.controller;

import com.chatmulticanale.dao.CanaleProgettoDAO;
import com.chatmulticanale.dao.ChatPrivataDAO;
import com.chatmulticanale.dao.MessaggioDAO;
import com.chatmulticanale.dto.MessaggioDTO;
import com.chatmulticanale.model.CanaleProgetto;

import java.sql.SQLException;
import java.util.List;

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
     * Gestisce la logica per avviare una nuova chat privata.
     *
     * @param idMessaggioOrigine L'ID del messaggio da cui partire.
     * @param idIniziatore L'ID dell'utente che avvia la chat.
     * @return true se la creazione ha successo, false altrimenti.
     */
    public boolean avviaNuovaChatPrivata(int idMessaggioOrigine, int idIniziatore) {
        try {
            chatPrivataDAO.avviaChatPrivata(idMessaggioOrigine, idIniziatore);
            return true;
        } catch (SQLException e) {
            // L'errore (es. chat con se stessi) viene loggato dal DAO.
            // Possiamo anche recuperare il messaggio di errore per la View se vogliamo.
            System.err.println("Errore SQL Controller: " + e.getMessage()); // Aggiungiamo un log per il debug
            return false;
        }
    }
}