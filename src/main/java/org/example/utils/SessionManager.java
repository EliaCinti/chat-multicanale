package org.example.utils;

import org.example.model.Utente;

/**
 * Singleton per gestire la sessione dell'utente.
 * Mantiene in memoria l'utente che ha effettuato il login.
 */
public final class SessionManager {
    private static SessionManager instance; // L'unica istanza della classe

    private Utente utenteLoggato; // L'utente attualmente loggato

    // Il costruttore privato impedisce di creare istanze dall'esterno
    private SessionManager() {}

    /**
     * Restituisce l'unica istanza della classe.
     * Se non esiste, la crea. (Lazy initialization)
     * @return L'istanza di SessionManager.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Registra l'utente che ha effettuato il login.
     * @param utente L'oggetto Utente restituito dal DAO dopo un login corretto.
     */
    public void login(Utente utente) {
        this.utenteLoggato = utente;
    }

    /**
     * Effettua il logout, rimuovendo l'utente dalla sessione.
     */
    public void logout() {
        this.utenteLoggato = null;
    }

    /**
     * Restituisce l'utente attualmente loggato.
     * @return L'oggetto Utente loggato, o null se nessuno è loggato.
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    /**
     * Controlla se c'è un utente loggato.
     * @return true se un utente è loggato, false altrimenti.
     */
    public boolean isUserLoggedIn() {
        return utenteLoggato != null;
    }
}
