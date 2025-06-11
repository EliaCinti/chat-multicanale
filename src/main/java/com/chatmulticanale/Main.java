package com.chatmulticanale;

import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.controller.SignUpController;
import com.chatmulticanale.utils.DatabaseConnector;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.WelcomeView;
import com.chatmulticanale.view.navigation.NavigationManager;

/**
 * Classe principale e punto d'ingresso (Entry Point) dell'applicazione.
 * Ha la responsabilità di:
 * 1. Inizializzare i componenti di base (controller).
 * 2. Creare e avviare il gestore di navigazione.
 * 3. Eseguire la pulizia finale delle risorse quando l'applicazione termina.
 */
public class Main {
    public static void main(String[] args) {
        // 1. Crea tutti i controller di cui la WelcomeView avrà bisogno.
        LoginController loginController = new LoginController();
        SignUpController signUpController = new SignUpController();

        // 2. Crea la vista di partenza e le passa le sue dipendenze.
        WelcomeView welcomeView = new WelcomeView(loginController, signUpController);

        NavigationManager navManager = new NavigationManager();
        navManager.start(welcomeView);

        // 3. Pulisce le risorse alla fine.
        InputUtils.closeScanner();
        DatabaseConnector.closeConnection();
        ViewUtils.println("\nProgramma terminato. Arrivederci!");
    }
}
