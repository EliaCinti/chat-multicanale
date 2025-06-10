package com.chatmulticanale.view;

import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.ColorUtils; // Manteniamo i tuoi colori!
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Vista specializzata ESCLUSIVAMENTE nella gestione del form di login.
 * Viene lanciata dalla WelcomeView e, in caso di successo, naviga
 * verso la home page appropriata per il ruolo dell'utente.
 */
public class LoginView implements View { // <-- Implementa la nostra interfaccia
    private final LoginController loginController;

    /**
     * Costruttore che riceve il controller necessario.
     * @param controller Il controller che gestisce la logica di autenticazione.
     */
    public LoginView(LoginController controller) {
        this.loginController = controller;
    }

    /**
     * Metodo principale della vista, come richiesto dall'interfaccia View.
     * Mostra il form di login e gestisce il ciclo di autenticazione.
     * @return Un oggetto Navigazione che indica cosa fare dopo.
     */
    @Override
    public Navigazione show() {
        // Usiamo un loop per permettere all'utente di riprovare in caso di errore
        // o di tornare indietro.
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + ColorUtils.ANSI_GREEN + "--- LOGIN ---" + ColorUtils.ANSI_RESET);
            ViewUtils.printSeparator();

            // Raccoglie le credenziali
            String username = InputUtils.readString("Username (o '0' per tornare indietro): ");
            // Se l'utente vuole tornare indietro subito
            if (username.equals("0")) {
                return Navigazione.indietro();
            }

            String password = InputUtils.readString("Password: ");

            // Chiama il controller per tentare l'autenticazione
            Utente utenteAutenticato = loginController.autentica(username, password);

            // Valuta il risultato
            if (utenteAutenticato != null) {
                // SUCCESSO!
                ViewUtils.println("\nLogin effettuato con successo!");
                // Ora dobbiamo navigare alla home page corretta.
                // Usiamo un metodo helper per decidere quale vista creare.
                return Navigazione.vaiA(getHomeViewPerRuolo(utenteAutenticato));
            } else {
                // FALLIMENTO!
                ViewUtils.println(ColorUtils.ANSI_RED + "\nCredenziali non valide. Riprova." + ColorUtils.ANSI_RESET);
                InputUtils.readString("Premi Invio per continuare...");
            }
        }
    }

    /**
     * Metodo helper privato che crea e restituisce la vista "Home" corretta
     * in base al ruolo dell'utente che ha appena effettuato il login.
     * Questo centralizza la logica di "smistamento post-login".
     *
     * @param utente L'utente autenticato.
     * @return La View appropriata per la home page dell'utente.
     */
    private View getHomeViewPerRuolo(Utente utente) {
        return switch (utente.getRuolo()) {
            case dipendente -> {
                // Crea i controller necessari per la DipendenteHomeView
                InterazioneUtenteController iuc = new InterazioneUtenteController();
                yield new DipendenteHomeView(iuc);
            }
            case capoprogetto ->
                // Quando implementerai questa parte, creerai i controller qui
                // GestioneProgettiController gpc = new GestioneProgettiController();
                // InterazioneUtenteController iuc_cp = new InterazioneUtenteController();
                // return new CapoProgettoHomeView(iuc_cp, gpc);
                // TODO
                    new StubView("Home del Capo Progetto non ancora implementata.");
            case amministratore ->
                // Quando implementerai questa parte, creerai i controller qui
                // AmministrazioneController ac = new AmministrazioneController();
                // return new AdminHomeView(ac);
                // TODO
                    new StubView("Home dell'Amministratore non ancora implementata.");
        };
    }
}