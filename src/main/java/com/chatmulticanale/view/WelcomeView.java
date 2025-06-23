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
 * La vista di benvenuto. È speciale: non è una vista "navigabile" ma funge
 * da router iniziale per l'intera applicazione.
 */
public class WelcomeView implements View {
    private final LoginController loginController;
    private final SignUpController signUpController;

    public WelcomeView(LoginController loginCtrl, SignUpController signUpCtrl) {
        this.loginController = loginCtrl;
        this.signUpController = signUpCtrl;
    }

    /**
     * Mostra il menu di benvenuto e restituisce la prima vista della sessione
     * che l'utente ha scelto di avviare.
     * @return La prossima View da mostrare, o null se l'utente sceglie di uscire.
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
                    // Passa il LoginController alla LoginView
                    return Navigazione.vaiA(new LoginView(this.loginController));
                case 2:
                    // Passa il SignUpController alla SignUpView
                    return Navigazione.vaiA(new SignUpView(this.signUpController));
                case 0:
                    return Navigazione.exit();
            }
        }
    }
}