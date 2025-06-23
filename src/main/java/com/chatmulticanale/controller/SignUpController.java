package com.chatmulticanale.controller;

import com.chatmulticanale.dao.UtenteDAO;
import com.chatmulticanale.model.Ruolo;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.PasswordUtils;
import java.sql.SQLException;

/**
 * Controller dedicato al processo di registrazione degli utenti (Sign Up).
 * Gestisce la creazione di un nuovo utente con ruolo di default 'Dipendente'.
 */
public class SignUpController {
    private final UtenteDAO utenteDAO = new UtenteDAO();

    /**
     * Registra un nuovo utente pubblico con credenziali e dati anagrafici.
     * Esegue l'hash della password, crea l'oggetto Utente e lo salva nel database.
     * Il ruolo assegnato di default è 'Dipendente' per motivi di sicurezza.
     *
     * @param username  nome utente scelto dall'utente (deve essere univoco)
     * @param password  password in chiaro fornita dall'utente
     * @param nome      nome proprio dell'utente
     * @param cognome   cognome dell'utente
     * @return {@code true} se la registrazione ha successo;
     *         {@code false} in caso di errore (es. Username già esistente o altre violazioni di vincoli SQL)
     * @see PasswordUtils#hashPassword(String)
     * @see UtenteDAO#creaNuovoUtente(Utente)
     */
    public boolean registraNuovoUtente(String username, String password, String nome, String cognome) {
        // 1. Hash della password prima di salvarla
        String passwordHash = PasswordUtils.hashPassword(password);
        if (username.equals("0")){
            return false;
        }
        // 2. Creazione dell'oggetto Utente
        Utente nuovoUtente = new Utente();
        nuovoUtente.setUsername(username);
        nuovoUtente.setPassword(passwordHash);
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setRuolo(Ruolo.dipendente); // Ruolo fisso per la registrazione

        // 3. Salvataggio nel database tramite il DAO
        try {
            utenteDAO.creaNuovoUtente(nuovoUtente);
            return true;
        } catch (SQLException e) {
            // L'errore più comune qui è la violazione del vincolo UNIQUE sull'username.
            return false;
        }
    }
}