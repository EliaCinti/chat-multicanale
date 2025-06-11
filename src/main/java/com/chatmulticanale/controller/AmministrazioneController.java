package com.chatmulticanale.controller;

import com.chatmulticanale.dao.UtenteDAO;
import com.chatmulticanale.model.Utente;
import java.util.List;

/**
 * Controller per le operazioni di amministrazione del sistema.
 * Contiene la logica di business per le azioni riservate all'amministratore.
 */
public class AmministrazioneController {
    private final UtenteDAO utenteDAO = new UtenteDAO();

    /**
     * Recupera la lista di tutti gli utenti che possono essere promossi (i Dipendenti).
     * @return Una lista di oggetti Utente.
     */
    public List<Utente> getListaUtentiPromuovibili() {
        return utenteDAO.getTuttiDipendenti();
    }

    /**
     * Gestisce la logica di business per promuovere un utente.
     * @param idUtente L'ID dell'utente da promuovere.
     * @return true se l'operazione ha successo, false altrimenti.
     */
    public boolean promuoviUtenteACapoProgetto(int idUtente) {
        try {
            utenteDAO.assegnaRuoloCapoProgetto(idUtente);
            return true;
        } catch (Exception e) {
            // Se il DAO lancia un'eccezione, la promozione è fallita.
            return false;
        }
    }
}