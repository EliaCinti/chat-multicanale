package com.chatmulticanale.model;

/**
 * Rappresenta un utente del sistema con le informazioni principali memorizzate nel database.
 * Include credenziali, dati anagrafici e ruolo applicativo per gestire autorizzazioni.
 */
public class Utente {

    /**
     * Identificativo univoco dell'utente.
     */
    private int idUtente;

    /**
     * Username unico utilizzato per l'autenticazione.
     */
    private String username;

    /**
     * Password hashata dell'utente. Non deve essere mai trasmessa in chiaro.
     */
    private String password;

    /**
     * Nome proprio dell'utente.
     */
    private String nome;

    /**
     * Cognome dell'utente.
     */
    private String cognome;

    /**
     * Ruolo applicativo assegnato all'utente per la gestione dei permessi.
     */
    private Ruolo ruolo;

    // --- GETTER E SETTER ---

    /**
     * Restituisce l'ID univoco dell'utente.
     *
     * @return identificativo numerico dell'utente
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'ID univoco dell'utente.
     *
     * @param idUtente identificativo numerico da assegnare
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce lo username dell'utente.
     *
     * @return username univoco per autenticazione
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta lo username dell'utente.
     *
     * @param username username univoco da assegnare
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Restituisce la password hashata.
     *
     * @return hash della password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password hashata dell'utente.
     *
     * @param password hash della password da assegnare
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce il nome proprio dell'utente.
     *
     * @return nome anagrafico dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome proprio dell'utente.
     *
     * @param nome nome anagrafico da assegnare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return cognome anagrafico dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome cognome anagrafico da assegnare
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce il ruolo applicativo dell'utente.
     *
     * @return enum {@link Ruolo} assegnato all'utente
     */
    public Ruolo getRuolo() {
        return ruolo;
    }

    /**
     * Imposta il ruolo applicativo dell'utente.
     *
     * @param ruolo enum {@link Ruolo} da assegnare
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}