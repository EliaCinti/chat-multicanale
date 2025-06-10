package com.chatmulticanale.view;

import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.controller.SignUpController; // <-- NUOVO IMPORT
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

public class WelcomeView implements View {
    // La WelcomeView ora ha bisogno di entrambi i controller per poterli
    // passare alle viste figlie.
    private final LoginController loginController;
    private final SignUpController signUpController;

    public WelcomeView(LoginController loginCtrl, SignUpController signUpCtrl) {
        this.loginController = loginCtrl;
        this.signUpController = signUpCtrl;
    }

    @Override
    public Navigazione show() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + ColorUtils.ANSI_GREEN + "BENVENUTO in Chat-Multicanale!" + ColorUtils.ANSI_RESET);

        while (true) {
            ViewUtils.printSeparator();
            ViewUtils.println("1. Login");
            ViewUtils.println("2. Registrati (come Dipendente)");
            ViewUtils.println("0. Esci dall'applicazione");
            int scelta = InputUtils.readInt("Scelta: ");

            switch (scelta) {
                case 1:
                    // Passa il LoginController alla LoginView
                    return Navigazione.vaiA(new LoginView(this.loginController));
                case 2:
                    // Passa il SignUpController alla SignUpView
                    return Navigazione.vaiA(new SignUpView(this.signUpController));
                case 0:
                    return Navigazione.logout();
                default:
                    ViewUtils.println("Scelta non valida.");
            }
        }
    }
}