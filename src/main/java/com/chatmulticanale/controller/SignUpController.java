package com.chatmulticanale.controller;

import com.chatmulticanale.dao.UtenteDAO;
import com.chatmulticanale.model.Ruolo;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.PasswordUtils;
import com.chatmulticanale.utils.ViewUtils;
import java.sql.SQLException;

/**
 * Controller dedicato esclusivamente alla gestione del processo di registrazione (Sign Up).
 */
public class SignUpController {
    private final UtenteDAO utenteDAO = new UtenteDAO();

    /**
     * Orchestra il processo di registrazione per un nuovo utente pubblico.
     * Il ruolo viene assegnato di default a 'Dipendente' per motivi di sicurezza.
     *
     * @return true se la registrazione ha successo, false altrimenti.
     */
    public boolean registraNuovoUtente(String username, String password, String nome, String cognome) {
        // 1. Hash della password prima di salvarla
        String passwordHash = PasswordUtils.hashPassword(password);

        // 2. Creazione dell'oggetto Utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setUsername(username);
        nuovoUtente.setPassword(passwordHash);
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setRuolo(Ruolo.dipendente); // Ruolo fisso per la registrazione pubblica

        // 3. Salvataggio nel database tramite il DAO
        try {
            utenteDAO.creaNuovoUtente(nuovoUtente);
            ViewUtils.println("Registrazione completata con successo! Ora puoi effettuare il login.");
            return true;
        } catch (SQLException e) {
            // L'errore più comune qui è la violazione del vincolo UNIQUE sull'username.
            ViewUtils.println("ERRORE: Username già esistente. Scegline un altro.");
            return false;
        }
    }
}