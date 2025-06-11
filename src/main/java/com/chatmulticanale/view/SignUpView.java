package com.chatmulticanale.view;

import com.chatmulticanale.controller.SignUpController; // <-- CAMBIA L'IMPORT
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

public class SignUpView implements View {
    private final SignUpController signUpController;

    // Il costruttore ora accetta un SignUpController
    public SignUpView(SignUpController controller) {
        this.signUpController = controller;
    }

    @Override
    public Navigazione show() {
        ViewUtils.printSeparator();
        ViewUtils.println("--- REGISTRAZIONE NUOVO UTENTE ---");

        // Validazione per l'username: non deve contenere spazi
        String username = InputUtils.readValidatedString(
                "Scegli uno username: ",
                u -> !u.contains(" "), // Regola: restituisce true se l'username NON contiene spazi
                "Errore: Lo username non può contenere spazi."
        );

        // Validazione per la password: deve essere lunga almeno 8 caratteri
        String password = InputUtils.readValidatedString(
                "Scegli una password (almeno 8 caratteri): ",
                p -> p.length() >= 8, // Regola: restituisce true se la lunghezza è >= 8
                "Errore: La password deve essere di almeno 8 caratteri."
        );

        String nome = InputUtils.readString("Il tuo nome: ");
        String cognome = InputUtils.readString("Il tuo cognome: ");

        // Chiama il metodo del nuovo controller
        signUpController.registraNuovoUtente(username, password, nome, cognome);

        InputUtils.pressEnterToContinue("\nPremi Invio per tornare alla schermata precedente...");
        return Navigazione.indietro();
    }
}
