package com.chatmulticanale;

import com.chatmulticanale.controller.LoginController;
import com.chatmulticanale.controller.SignUpController;
import com.chatmulticanale.utils.DatabaseConnector;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.WelcomeView;
import com.chatmulticanale.view.navigation.NavigationManager;

/**
 * Classe principale e punto d'ingresso dell'applicazione Chat-Multicanale.
 * <p>
 * Si occupa di:
 * <ol>
 *   <li>Inizializzare i controller di login e registrazione.</li>
 *   <li>Creare la vista di benvenuto e avviare il ciclo di navigazione tramite {@link NavigationManager}.</li>
 *   <li>Effettuare la pulizia delle risorse (scanner, connessione DB) al termine dell'esecuzione.</li>
 * </ol>
 */
public class Main {

    /**
     * Entry point dell'applicazione.
     * <p>
     * Inizializza i controller e la vista di benvenuto, quindi avvia
     * il {@link NavigationManager} per gestire il flusso di navigazione.
     * Al termine, chiude lo scanner di input e la connessione al database.
     *
     * @param args argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Inizializza i controller di login e registrazione
        LoginController loginController = new LoginController();
        SignUpController signUpController = new SignUpController();

        // Crea la vista di benvenuto con le dipendenze
        WelcomeView welcomeView = new WelcomeView(loginController, signUpController);

        // Avvia il gestore di navigazione con la welcome view
        NavigationManager navManager = new NavigationManager();
        navManager.start(welcomeView);

        // Pulisce le risorse al termine
        InputUtils.closeScanner();
        DatabaseConnector.closeConnection();
        ViewUtils.println("\nProgramma terminato. Arrivederci!");
    }
}