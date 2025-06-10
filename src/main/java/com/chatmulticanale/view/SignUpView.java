package com.chatmulticanale.view;

import com.chatmulticanale.controller.SignUpController; // <-- CAMBIA L'IMPORT
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

public class SignUpView implements View {
    private final SignUpController signUpController; // <-- CAMBIA IL TIPO

    // Il costruttore ora accetta un SignUpController
    public SignUpView(SignUpController controller) {
        this.signUpController = controller;
    }

    @Override
    public Navigazione show() {
        ViewUtils.printSeparator();
        ViewUtils.println("--- REGISTRAZIONE NUOVO UTENTE ---");

        String username = InputUtils.readString("Scegli uno username: ");
        String password = InputUtils.readString("Scegli una password: ");
        String nome = InputUtils.readString("Il tuo nome: ");
        String cognome = InputUtils.readString("Il tuo cognome: ");

        // Chiama il metodo del nuovo controller
        signUpController.registraNuovoUtente(username, password, nome, cognome);

        InputUtils.readString("\nPremi Invio per tornare alla schermata precedente...");
        return Navigazione.indietro();
    }
}
