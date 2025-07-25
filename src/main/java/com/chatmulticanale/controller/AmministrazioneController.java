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

    /**
     * Recupera la lista di tutti gli utenti che possono essere promossi (i Dipendenti).
     * @return Una lista di oggetti Utente.
     */
    public List<Utente> getListaUtentiPromuovibili() {
        return utenteDAO.getTuttiDipendenti();
    }

    /**
     * Promuove un utente al ruolo di capo progetto.
     *
     * @param idUtente identificativo dell'utente da promuovere
     * @return {@code true} se l'operazione va a buon fine, {@code false} in caso di errore
     */
    public boolean promuoviUtenteACapoProgetto(int idUtente) {
        try {
            utenteDAO.assegnaRuoloCapoProgetto(idUtente);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Restituisce tutti gli utenti attualmente con il ruolo di capo progetto.
     *
     * @return lista di {@link Utente} che ricoprono il ruolo di capo progetto
     * @see UtenteDAO#getTuttiCapiProgetto()
     */
    public List<Utente> getListaCapiProgetto() {
        return utenteDAO.getTuttiCapiProgetto();
    }

    /**
     * Recupera i progetti assegnati a un determinato responsabile.
     *
     * @param idResponsabile identificativo del capo progetto
     * @return lista di {@link Progetto} di cui l'utente è responsabile
     * @see ProgettoDAO#trovaProgettiPerResponsabile(int)
     */
    public List<Progetto> getProgettiDiUnResponsabile(int idResponsabile) {
        return progettoDAO.trovaProgettiPerResponsabile(idResponsabile);
    }

    /**
     * Finalizza il processo di degradazione di un capo progetto, rimuovendo il ruolo corrispondente.
     *
     * @param idUtente identificativo dell'utente da degradare
     * @return {@code true} se il degrado viene completato con successo, {@code false} in caso di errore SQL
     */
    public boolean finalizzaDegradoCapoProgetto(int idUtente) {
        try {
            utenteDAO.rimuoviRuoloCapoProgetto(idUtente);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Recupera i progetti che non hanno ancora un responsabile assegnato.
     *
     * @return lista di {@link Progetto} non assegnati
     * @see ProgettoDAO#getProgettiNonAssegnati()
     */
    public List<Progetto> getListaProgettiNonAssegnati() {
        return progettoDAO.getProgettiNonAssegnati();
    }

    /**
     * Assegna un progetto a un capo progetto per la prima volta.
     * Utilizza la stored procedure {@code sp_AM3} lato database.
     *
     * @param idProgetto       identificativo del progetto da assegnare
     * @param idCapoProgetto   identificativo dell'utente designato come responsabile
     * @return {@code true} se l'assegnazione ha successo, {@code false} in caso di errore SQL
     */
    public boolean assegnaProgetto(int idProgetto, int idCapoProgetto) {
        try {
            progettoDAO.assegnaResponsabilitaProgetto(idProgetto, idCapoProgetto);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Restituisce una lista di progetti con i relativi responsabili, strutturata in DTO per la visualizzazione.
     *
     * @return lista di {@link ProgettoResponsabileDTO} che aggregano i dati del progetto e del responsabile
     * @see ProgettoDAO#getProgettiConResponsabile()
     */
    public List<ProgettoResponsabileDTO> getListaProgettiConResponsabile() {
        return progettoDAO.getProgettiConResponsabile();
    }

    /**
     * Riassegna un progetto a un nuovo responsabile.
     *
     * @param idProgetto             identificativo del progetto da riassegnare
     * @param idNuovoResponsabile    identificativo del nuovo capo progetto
     * @return {@code true} se l'aggiornamento viene eseguito con successo, {@code false} in caso di errore SQL
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
     * Crea un nuovo progetto con nome e descrizione specificati.
     *
     * @param nome         nome del progetto da creare
     * @param descrizione  descrizione del progetto da creare
     * @return {@code true} se la creazione va a buon fine, {@code false} in caso di errore SQL
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
