package com.chatmulticanale;

import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.controller.SignUpController;
import com.chatmulticanale.utils.DatabaseConnector;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.WelcomeView;
import com.chatmulticanale.view.navigation.NavigationManager;

public class Main {
    public static void main(String[] args) {
        // 1. Crea tutti i controller di cui le viste iniziali avranno bisogno.
        LoginController loginController = new LoginController();
        SignUpController signUpController = new SignUpController();

        // 2. Crea la vista di partenza e le passa le sue dipendenze.
        WelcomeView welcomeView = new WelcomeView(loginController, signUpController);

        // 3. Avvia il gestore di navigazione con la WelcomeView.
        NavigationManager navManager = new NavigationManager();
        navManager.start(welcomeView);

        // 4. Pulisce le risorse alla fine.
        InputUtils.closeScanner();
        DatabaseConnector.closeConnection();
        ViewUtils.println("\nProgramma terminato. Arrivederci!");
    }
}
