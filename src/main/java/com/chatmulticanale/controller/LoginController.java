package com.chatmulticanale.controller;

import com.chatmulticanale.dao.UtenteDAO;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.PasswordUtils;
import com.chatmulticanale.utils.SessionManager;

/**
 * Controller responsabile della gestione dell'autenticazione degli utenti.
 * Verifica le credenziali e gestisce la sessione dell'utente.
 */
public class LoginController {
    private final UtenteDAO utenteDAO = new UtenteDAO();

    /**
     * Autentica un utente tramite username e password.
     * Se le credenziali sono valide, avvia una sessione e restituisce l'utente autenticato.
     *
     * @param username username fornito dall'utente
     * @param password password in chiaro fornita dall'utente
     * @return l'oggetto {@link Utente} autenticato se le credenziali sono corrette,
     *         {@code null} in caso di fallimento dell'autenticazione
     * @see UtenteDAO#getUtenteByUsername(String)
     * @see PasswordUtils#checkPassword(String, String)
     * @see SessionManager#login(Utente)
     */
    public Utente autentica(String username, String password) {
        Utente utenteDalDB = utenteDAO.getUtenteByUsername(username);

        if (utenteDalDB != null && PasswordUtils.checkPassword(password, utenteDalDB.getPassword())) {
            SessionManager.getInstance().login(utenteDalDB);
            return utenteDalDB;
        }
        return null; // Autenticazione fallita
    }
}