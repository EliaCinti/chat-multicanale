package com.chatmulticanale.view;

import com.chatmulticanale.controller.AmministrazioneController;
import com.chatmulticanale.controller.GestioneProgettiController;
import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.exception.CommandException; // <-- NUOVO IMPORT
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

public class LoginView implements View {
    private final LoginController loginController;

    public LoginView(LoginController controller) {
        this.loginController = controller;
    }

    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.LOGIN + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK_2);
            ViewUtils.printSeparator();

            try {
                // si username e password
                String username = InputUtils.askForInput(CostantiView.USERNAME);
                String password = InputUtils.askForInput(CostantiView.PASSWORD);

                Utente utenteAutenticato = loginController.autentica(username, password);

                if (utenteAutenticato != null) {
                    ViewUtils.println(ColorUtils.ANSI_GREEN + CostantiView.LOGIN_SI + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
                    ViewUtils.clearScreen();
                    return Navigazione.vaiA(getHomeViewPerRuolo(utenteAutenticato));
                } else {
                    ViewUtils.println(ColorUtils.ANSI_RED + CostantiView.LOGIN_NO + ColorUtils.ANSI_RESET);
                    InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
                    // Se il login fallisce, il loop while ricomincerà.
                }

            } catch (CommandException e) {
                // Se l'utente digita un comando, l'eccezione viene catturata
                // e noi obbediamo all'istruzione di navigazione.
                return e.getNavigazione();
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
                InterazioneUtenteController iuc = new InterazioneUtenteController();
                yield new DipendenteView(iuc);
            }
            case capoprogetto -> {
                GestioneProgettiController gpc = new GestioneProgettiController();
                InterazioneUtenteController iuc = new InterazioneUtenteController();
                yield new CapoProgettoView(gpc, iuc);
            }
            case amministratore -> {
                AmministrazioneController adminController = new AmministrazioneController();
                yield new AmministrazioneView(adminController);
            }
        };
    }
}