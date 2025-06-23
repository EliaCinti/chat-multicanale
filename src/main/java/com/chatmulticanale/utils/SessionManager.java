package com.chatmulticanale.utils;

import com.chatmulticanale.model.Utente;

/**
 * Singleton per gestire lo stato della sessione utente nell'applicazione.
 * Mantiene in memoria le informazioni sull'utente che ha effettuato il login.
 * Utilizza l'inizializzazione "lazy" per creare l'istanza solo quando necessaria.
 */
public final class SessionManager {

    /**
     * Unica istanza di SessionManager.
     */
    private static SessionManager instance;

    /**
     * Utente attualmente autenticato nella sessione.
     */
    private Utente utenteLoggato;

    // Costruttore privato per evitare istanziazione esterna
    private SessionManager() {}

    /**
     * Restituisce l'istanza singleton di SessionManager.
     * Se non esiste ancora, ne crea una nuova.
     *
     * @return istanza di {@link SessionManager}
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Registra l'utente che ha effettuato il login nella sessione corrente.
     *
     * @param utente oggetto {@link Utente} autenticato
     */
    public void login(Utente utente) {
        this.utenteLoggato = utente;
    }

    /**
     * Effettua il logout rimuovendo l'utente dalla sessione.
     * Dopo la chiamata, non c'è alcun utente autenticato.
     */
    public void logout() {
        this.utenteLoggato = null;
    }

    /**
     * Restituisce l'utente attualmente loggato.
     *
     * @return oggetto {@link Utente} autenticato o {@code null} se nessuno è loggato
     */
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }
}
