package com.chatmulticanale.controller;

import com.chatmulticanale.dao.*;
import com.chatmulticanale.dto.ChatSupervisioneDTO;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.Utente;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Controller per la gestione delle operazioni sui progetti e sui relativi canali.
 * Fornisce metodi per creare e gestire canali di progetto, amministrare i membri
 * e supervisionare le chat private all'interno di un progetto.
 * <p>
 * Utilizzato dalle viste dedicate ai Capi Progetto.
 */
public class GestioneProgettiController {

    private final ProgettoDAO progettoDAO = new ProgettoDAO();
    private final CanaleProgettoDAO canaleDAO = new CanaleProgettoDAO();
    private final UtenteDAO utenteDAO = new UtenteDAO();
    private final PartecipaCanaleDAO partecipaDAO = new PartecipaCanaleDAO();
    private final ChatPrivataDAO chatPrivataDAO = new ChatPrivataDAO();

    /**
     * Restituisce la lista dei progetti di cui un Capo Progetto è responsabile.
     *
     * @param idCapoProgetto identificativo dell'utente con ruolo Capo Progetto
     * @return lista di {@link Progetto} gestiti dal capo progetto
     * @see ProgettoDAO#trovaProgettiPerResponsabile(int)
     */
    public List<Progetto> getProgettiDiCuiSonoResponsabile(int idCapoProgetto) {
        return progettoDAO.trovaProgettiPerResponsabile(idCapoProgetto);
    }

    /**
     * Crea un nuovo canale all'interno di un progetto.
     *
     * @param nomeCanale        nome del canale da creare
     * @param descrizioneCanale descrizione del canale
     * @param idProgetto        identificativo del progetto di appartenenza
     * @param idCreatore        identificativo del Capo Progetto creatore del canale
     * @return {@code true} se il canale è stato creato correttamente, {@code false} in caso di errore SQL
     * @see CanaleProgettoDAO#creaNuovoCanale(CanaleProgetto, int, int)
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

    /**
     * Recupera tutti i canali associati a un determinato progetto.
     *
     * @param idProgetto identificativo del progetto
     * @return lista di {@link CanaleProgetto} appartenenti al progetto
     * @see CanaleProgettoDAO#getCanaliPerProgetto(int)
     */
    public List<CanaleProgetto> getCanaliDelProgetto(int idProgetto) {
        return canaleDAO.getCanaliPerProgetto(idProgetto);
    }

    /**
     * Restituisce i dipendenti che non partecipano ancora a un dato canale.
     * Utile per mostrare chi è aggiungibile.
     *
     * @param idCanale identificativo del canale
     * @return lista di {@link Utente} non ancora presenti nel canale
     * @see UtenteDAO#getDipendentiNonInCanale(int)
     */
    public List<Utente> getDipendentiAggiungibili(int idCanale) {
        return utenteDAO.getDipendentiNonInCanale(idCanale);
    }

    /**
     * Aggiunge un dipendente a un canale di progetto.
     *
     * @param idCanale identificativo del canale
     * @param idUtente identificativo del dipendente da aggiungere
     * @return {@code true} se l'aggiunta avviene con successo, {@code false} in caso di errore SQL
     * @see PartecipaCanaleDAO#aggiungiUtenteACanale(int, int)
     */
    public boolean aggiungiDipendenteACanale(int idCanale, int idUtente) {
        try {
            partecipaDAO.aggiungiUtenteACanale(idCanale, idUtente);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Recupera la lista dei dipendenti attualmente partecipanti a un canale.
     *
     * @param idCanale identificativo del canale
     * @return lista di {@link Utente} membri del canale
     * @see UtenteDAO#getDipendentiInCanale(int)
     */
    public List<Utente> getDipendentiDelCanale(int idCanale) {
        return utenteDAO.getDipendentiInCanale(idCanale);
    }

    /**
     * Rimuove un dipendente da un canale di progetto.
     *
     * @param idCanale identificativo del canale
     * @param idUtente identificativo del dipendente da rimuovere
     * @return {@code true} se la rimozione è avvenuta con successo, {@code false} in caso di errore SQL
     * @see PartecipaCanaleDAO#rimuoviUtenteDaCanale(int, int)
     */
    public boolean rimuoviDipendenteDaCanale(int idCanale, int idUtente) {
        try {
            partecipaDAO.rimuoviUtenteDaCanale(idCanale, idUtente);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Recupera le chat private di un progetto per consentire la supervisione.
     * In caso di errore (es. permessi negati o errore DB), restituisce lista vuota.
     *
     * @param idProgetto     identificativo del progetto di riferimento
     * @param idCapoProgetto identificativo dell'utente supervisore
     * @return lista di {@link ChatSupervisioneDTO} contenenti i messaggi da supervisionare
     * @see ChatPrivataDAO#getChatDaSupervisionare(int, int)
     */
    public List<ChatSupervisioneDTO> getChatPrivateDaSupervisionare(int idProgetto, int idCapoProgetto) {
        try {
            return chatPrivataDAO.getChatDaSupervisionare(idProgetto, idCapoProgetto);
        } catch (SQLException e) {
            // Se il DAO lancia un'eccezione (es. Per permessi negati dalla SP),
            // il controller restituisce null (lista vuota) per segnalare l'errore alla View.
            return Collections.emptyList();
        }
    }
}

