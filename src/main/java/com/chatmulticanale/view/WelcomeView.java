package com.chatmulticanale.view;

import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.controller.SignUpController;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Vista di benvenuto dell'applicazione.
 * <p>
 * Funziona da router iniziale, permettendo all'utente di scegliere
 * tra login, registrazione o uscita dall'applicazione.
 * Non è una vista "navigabile" nel senso classico,
 * ma restituisce la navigazione verso la vista successiva.
 */
public class WelcomeView implements View {

    /**
     * Controller responsabile della logica di autenticazione.
     */
    private final LoginController loginController;

    /**
     * Controller responsabile della logica di registrazione.
     */
    private final SignUpController signUpController;

    /**
     * Costruisce la vista di benvenuto con i controller necessari.
     *
     * @param loginCtrl controller per gestire il login
     * @param signUpCtrl controller per gestire la registrazione
     */
    public WelcomeView(LoginController loginCtrl, SignUpController signUpCtrl) {
        this.loginController = loginCtrl;
        this.signUpController = signUpCtrl;
    }

    /**
     * Mostra il menu di benvenuto e gestisce la scelta iniziale dell'utente.
     * <p>
     * Offre le opzioni:
     * <ol>
     *   <li>Login</li>
     *   <li>Registrazione come Dipendente</li>
     *   <li>Uscita dall'applicazione</li>
     * </ol>
     * Gestisce input non validi e comandi di navigazione tramite {@link CommandException}.
     *
     * @return {@link Navigazione} che indica la vista successiva o l'uscita
     */
    @Override
    public Navigazione show() {
        while (true) {
            ViewUtils.clearScreen();
            ViewUtils.println(ColorUtils.ANSI_BLUE + "BENVENUTO in Chat-Multicanale!" + ColorUtils.ANSI_RESET);
            ViewUtils.printSeparator();
            ViewUtils.println("1. Login");
            ViewUtils.println("2. Registrati (come Dipendente)");
            ViewUtils.println("0. Esci dall'applicazione");

            int scelta;
            try {
                scelta = InputUtils.readIntInRange(CostantiView.SELEZIONA_OPZIONE, 0, 2);
            } catch (CommandException e) {
                if (e.getNavigazione().azione == Navigazione.Azione.LOGOUT) {
                    return e.getNavigazione();
                }
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Inserisci un numero valido." + ColorUtils.ANSI_RESET);
                InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_RIPROVARE);
                continue;
            }

            switch (scelta) {
                case 1:
                    // Naviga alla vista di login
                    return Navigazione.vaiA(new LoginView(loginController));
                case 2:
                    // Naviga alla vista di registrazione
                    return Navigazione.vaiA(new SignUpView(signUpController));
                case 0:
                    // Esci dall'applicazione
                    return Navigazione.exit();
                default:
                    // Non dovrebbe mai accadere grazie al readIntInRange
                    break;
            }
        }
    }
}