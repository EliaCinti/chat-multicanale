package com.chatmulticanale.controller;

import com.chatmulticanale.dao.*;
import com.chatmulticanale.dto.ChatSupervisioneDTO;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.Utente;

import java.sql.SQLException;
import java.util.List;

public class GestioneProgettiController {

    private final ProgettoDAO progettoDAO = new ProgettoDAO();
    private final CanaleProgettoDAO canaleDAO = new CanaleProgettoDAO();
    private final UtenteDAO utenteDAO = new UtenteDAO();
    private final PartecipaCanaleDAO partecipaDAO = new PartecipaCanaleDAO();
    private final ChatPrivataDAO chatPrivataDAO = new ChatPrivataDAO();

    /**
     * Recupera la lista dei progetti di cui un Capo Progetto specifico è responsabile.
     * @param idCapoProgetto L'ID dell'utente loggato.
     * @return Una lista di oggetti Progetto.
     */
    public List<Progetto> getProgettiDiCuiSonoResponsabile(int idCapoProgetto) {
        return progettoDAO.trovaProgettiPerResponsabile(idCapoProgetto);
    }

    /**
     * Gestisce la logica di business per la creazione di un nuovo canale.
     *
     * @param nomeCanale Il nome del nuovo canale.
     * @param descrizioneCanale La descrizione del nuovo canale.
     * @param idProgetto L'ID del progetto a cui appartiene.
     * @param idCreatore L'ID del Capo Progetto che lo crea.
     * @return true se la creazione ha successo, false altrimenti.
     */
    public boolean creaNuovoCanalePerProgetto(String nomeCanale, String descrizioneCanale, int idProgetto, int idCreatore) {
        CanaleProgetto canale = new CanaleProgetto();
        canale.setNomeCanale(nomeCanale);
        canale.setDescrizioneCanale(descrizioneCanale);

        try {
            canaleDAO.creaNuovoCanale(canale, idProgetto, idCreatore);
            return true;
        } catch (SQLException e) {
            // L'errore è già stato loggato dal DAO.
            return false;
        }
    }

    public List<CanaleProgetto> getCanaliDelProgetto(int idProgetto) {
        return canaleDAO.getCanaliPerProgetto(idProgetto);
    }

    public List<Utente> getDipendentiAggiungibili(int idCanale) {
        return utenteDAO.getDipendentiNonInCanale(idCanale);
    }

    public boolean aggiungiDipendenteACanale(int idCanale, int idUtente) {
        try {
            partecipaDAO.aggiungiUtenteACanale(idCanale, idUtente);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Utente> getDipendentiDelCanale(int idCanale) {
        return utenteDAO.getDipendentiInCanale(idCanale);
    }

    public boolean rimuoviDipendenteDaCanale(int idCanale, int idUtente) {
        try {
            partecipaDAO.rimuoviUtenteDaCanale(idCanale, idUtente);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Recupera le chat private di un progetto per la supervisione.
     *
     * @param idProgetto L'ID del progetto.
     * @param idCapoProgetto L'ID dell'utente che richiede la supervisione.
     * @return Una lista di {@link ChatSupervisioneDTO} o null in caso di errore di autorizzazione/DB.
     */
    public List<ChatSupervisioneDTO> getChatPrivateDaSupervisionare(int idProgetto, int idCapoProgetto) {
        try {
            return chatPrivataDAO.getChatDaSupervisionare(idProgetto, idCapoProgetto);
        } catch (SQLException e) {
            // Se il DAO lancia un'eccezione (es. Per permessi negati dalla SP),
            // il controller restituisce null per segnalare l'errore alla View.
            return null;
        }
    }
}

