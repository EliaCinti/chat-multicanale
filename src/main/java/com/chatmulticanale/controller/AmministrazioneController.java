package com.chatmulticanale.controller;

import com.chatmulticanale.dao.ProgettoDAO;
import com.chatmulticanale.dao.UtenteDAO;
import com.chatmulticanale.dto.ProgettoResponsabileDTO;
import com.chatmulticanale.model.Progetto;
import com.chatmulticanale.model.Utente;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller per le operazioni di amministrazione del sistema.
 * Contiene la logica di business per le azioni riservate all'amministratore.
 */
public class AmministrazioneController {
    private final UtenteDAO utenteDAO = new UtenteDAO();
    private final ProgettoDAO progettoDAO = new ProgettoDAO();

    // --- METODI PER AM1 (PROMUOVI UTENTE) ---
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

    // --- METODI PER AM2 (DEGRADA UTENTE) ---
    /**
     * Recupera la lista di tutti gli utenti che sono attualmente Capi Progetto,
     * per mostrarla all'amministratore.
     * @return Una lista di Utente.
     */
    public List<Utente> getListaCapiProgetto() {
        return utenteDAO.getTuttiCapiProgetto();
    }

    /**
     * Recupera la lista dei progetti gestiti da un utente specifico.
     * @param idResponsabile L'ID del capo progetto.
     * @return La lista dei suoi progetti.
     */
    public List<Progetto> getProgettiDiUnResponsabile(int idResponsabile) {
        return progettoDAO.trovaProgettiPerResponsabile(idResponsabile);
    }

    /**
     * Tenta di riassegnare un progetto a un nuovo responsabile.
     * @param idProgetto L'ID del progetto.
     * @param idNuovoResponsabile L'ID del nuovo capo.
     * @return true se ha successo, false altrimenti.
     */
    public boolean riassegnaProgetto(int idProgetto, int idNuovoResponsabile) {
        try {
            progettoDAO.aggiornaResponsabileProgetto(idProgetto, idNuovoResponsabile);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Esegue la logica completa per degradare un Capo Progetto.
     * ATTENZIONE: Questo metodo ora dipende dal flusso interattivo nella VISTA.
     * Il controller fornisce solo i pezzi. La VISTA dovrà orchestrare la logica che hai descritto.
     * Questo metodo esegue solo l'ultimo passo.
     *
     * @param idUtente L'ID dell'utente da degradare.
     * @return true se il degrado ha successo, false altrimenti.
     */
    public boolean finalizzaDegradoCapoProgetto(int idUtente) {
        try {
            utenteDAO.rimuoviRuoloCapoProgetto(idUtente);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // --- METODI PER AM3 (ASSEGNA RESPONSABILITA PROGETTO) ---
    /**
     * Recupera la lista dei progetti che non hanno ancora un responsabile.
     * @return Una lista di Progetto.
     */
    public List<Progetto> getListaProgettiNonAssegnati() {
        return progettoDAO.getProgettiNonAssegnati();
    }

    /**
     * Esegue la logica di business per assegnare un progetto a un Capo Progetto.
     * Riutilizza la logica di riassegnazione sottostante.
     * @param idProgetto L'ID del progetto da assegnare.
     * @param idCapoProgetto L'ID del Capo Progetto a cui assegnarlo.
     * @return true se l'operazione ha successo, false altrimenti.
     */
    public boolean assegnaProgetto(int idProgetto, int idCapoProgetto) {
        // Riutilizziamo il metodo "riassegnaProgetto" perché l'azione finale sul DB è la stessa!
        return riassegnaProgetto(idProgetto, idCapoProgetto);
    }

    // --- METODI PER AM4 (RIASSEGNA RESPONSABILITA PROGETTO)---
    /**
     * Recupera la lista di progetti assegnati come DTO per la visualizzazione.
     * Ogni DTO contiene i dati aggregati del progetto e del suo responsabile.
     * @return Una lista di {@link ProgettoResponsabileDTO}.
     */
    public List<ProgettoResponsabileDTO> getListaProgettiConResponsabile() {
        return progettoDAO.getProgettiConResponsabile();
    }

    // --- METODI PER AGGIUNGERE UN PROGETTO ---
    /**
     * Gestisce la logica per la creazione di un nuovo progetto.
     * @param nome Il nome del nuovo progetto.
     * @param descrizione La descrizione del nuovo progetto.
     * @return true se la creazione ha successo, false altrimenti.
     */
    public boolean creaNuovoProgetto(String nome, String descrizione) {
        Progetto nuovoProgetto = new Progetto();
        nuovoProgetto.setNomeProgetto(nome);
        nuovoProgetto.setDescrizioneProgetto(descrizione);

        try {
            progettoDAO.creaNuovoProgetto(nuovoProgetto);
            return true;
        } catch (SQLException e) {
            // Il DAO ha già loggato l'errore.
            return false;
        }
    }
}
