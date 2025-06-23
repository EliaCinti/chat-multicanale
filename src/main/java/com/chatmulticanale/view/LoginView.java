package com.chatmulticanale.view;

import com.chatmulticanale.controller.AmministrazioneController;
import com.chatmulticanale.controller.GestioneProgettiController;
import com.chatmulticanale.controller.InterazioneUtenteController;
import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.model.Utente;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Vista dedicata al processo di login.
 * <p>
 * Gestisce l'interfaccia per richiedere le credenziali dell'utente,
 * invocare il {@link LoginController} per l'autenticazione,
 * e reindirizzare alla home view appropriata in base al ruolo.
 */
public class LoginView implements View {

    /**
     * Controller responsabile della logica di autenticazione.
     */
    private final LoginController loginController;

    /**
     * Costruisce la vista di login con il controller specificato.
     *
     * @param controller instance di {@link LoginController} per gestire il login
     */
    public LoginView(LoginController controller) {
        this.loginController = controller;
    }

    /**
     * Mostra il form di login in loop fino a un esito o comando di navigazione.
     * <p>
     * Richiede username e password, chiama {@link LoginController#autentica},
     * e in caso di successo effettua il redirect alla home view tramite
     * {@link Navigazione#vaiA(View)}. In caso di comando di navigazione (/b, /q)
     * cattura {@link CommandException} e restituisce la navigazione corrispondente.
     *
     * @return {@link Navigazione} che indica l'azione successiva (redirect o logout)
     */
    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BOLD + CostantiView.LOGIN + ColorUtils.ANSI_RESET);
            ViewUtils.println(CostantiView.B_O_BACK_2);
            ViewUtils.printSeparator();

            try {
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
                }

            } catch (CommandException e) {
                // Se l'utente invia /b o /back, rispettiamo la navigazione richiesta
                return e.getNavigazione();
            }
        }
    }

    /**
     * Determina e restituisce la home view corretta in base al ruolo dell'utente.
     * <p>
     * Centralizza la logica di smistamento post-login:
     * <ul>
     *   <li>Dipendente -> {@link DipendenteView}</li>
     *   <li>CapoProgetto -> {@link CapoProgettoView}</li>
     *   <li>Amministratore -> {@link AmministrazioneView}</li>
     * </ul>
     *
     * @param utente oggetto {@link Utente} autenticato
     * @return {@link View} specifica per il ruolo
     * @throws IllegalArgumentException se il ruolo non è riconosciuto
     */
    private View getHomeViewPerRuolo(Utente utente) {
        switch (utente.getRuolo()) {
            case dipendente:
                InterazioneUtenteController iuc = new InterazioneUtenteController();
                return new DipendenteView(iuc);

            case capoprogetto:
                GestioneProgettiController gpc = new GestioneProgettiController();
                InterazioneUtenteController capIuc = new InterazioneUtenteController();
                return new CapoProgettoView(gpc, capIuc);

            case amministratore:
                AmministrazioneController adminController = new AmministrazioneController();
                return new AmministrazioneView(adminController);

            default:
                throw new IllegalArgumentException("Ruolo non riconosciuto: " + utente.getRuolo());
        }
    }
}