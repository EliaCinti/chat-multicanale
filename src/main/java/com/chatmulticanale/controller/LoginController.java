package com.chatmulticanale.controller;

import com.chatmulticanale.dao.UtenteDAO;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.PasswordUtils;
import com.chatmulticanale.utils.SessionManager;

/**
 * Controller responsabile della logica di autenticazione.
 */
public class LoginController {
    private final UtenteDAO utenteDAO = new UtenteDAO();

    /**
     * Autentica un utente confrontando le credenziali fornite.
     * @param username L'username inserito.
     * @param password La password in chiaro inserita.
     * @return L'oggetto Utente se l'autenticazione ha successo, altrimenti null.
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