package com.chatmulticanale.view;

import com.chatmulticanale.controller.SignUpController;
import com.chatmulticanale.exception.CommandException; // <-- NUOVO IMPORT
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Vista per la registrazione di un nuovo utente.
 * Ora gestisce i comandi di navigazione tramite CommandException.
 */
public class SignUpView implements View {
    private final SignUpController signUpController;

    public SignUpView(SignUpController controller) {
        this.signUpController = controller;
    }

    @Override
    public Navigazione show() {
        ViewUtils.clearScreen();
        ViewUtils.println(ColorUtils.ANSI_BOLD + "--- REGISTRAZIONE NUOVO UTENTE ---" + ColorUtils.ANSI_RESET);
        ViewUtils.println(CostantiView.B_O_BACK_2);
        ViewUtils.printSeparator();

        try {
            String username = InputUtils.askForInput(
                    "Scegli uno username: ",
                    u -> !u.contains(" ") && !u.startsWith("/"),
                    "ERRORE: Lo username non è valido (no spazi, no '/')."
            );

            String password = InputUtils.askForInput(
                    "Scegli una password (almeno 8 caratteri): ",
                    p -> p.length() >= 8,
                    "ERRORE: La password deve essere di almeno 8 caratteri."
            );

            String nome = InputUtils.askForInput("Il tuo nome: ");
            String cognome = InputUtils.askForInput("Il tuo cognome: ");

            // Se siamo arrivati qui, tutti i dati sono stati raccolti con successo.
            boolean registrazioneRiuscita = signUpController.registraNuovoUtente(username, password, nome, cognome);
            if (registrazioneRiuscita) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nRegistrazione completata con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Username non disponibile." + ColorUtils.ANSI_RESET);
            }
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
            return Navigazione.indietro();

        } catch (CommandException e) {
            // L'utente ha inserito un comando. Interrompiamo tutto e restituiamo
            // l'istruzione di navigazione contenuta nell'eccezione.
            return e.getNavigazione();
        }
    }
}
