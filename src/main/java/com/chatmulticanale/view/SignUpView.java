package com.chatmulticanale.view;

import com.chatmulticanale.controller.SignUpController;
import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.utils.ColorUtils;
import com.chatmulticanale.utils.InputUtils;
import com.chatmulticanale.utils.ViewUtils;
import com.chatmulticanale.view.costanti_view.CostantiView;
import com.chatmulticanale.view.navigation.Navigazione;
import com.chatmulticanale.view.navigation.View;

/**
 * Vista dedicata al processo di registrazione di un nuovo utente.
 * <p>
 * Gestisce l'interfaccia per raccogliere username, password, nome e cognome,
 * valida ogni campo tramite {@link InputUtils}, invoca il
 * {@link SignUpController} per creare l'utente, e restituisce
 * la navigazione successiva in base al risultato o ai comandi di navigazione.
 */
public class SignUpView implements View {

    /**
     * Controller responsabile della logica di registrazione.
     */
    private final SignUpController signUpController;

    /**
     * Costruisce la vista di registrazione con il controller specificato.
     *
     * @param controller istanza di {@link SignUpController} per gestire la creazione dell'utente
     */
    public SignUpView(SignUpController controller) {
        this.signUpController = controller;
    }

    /**
     * Mostra il form di registrazione e gestisce il flusso di raccolta dati.
     * <p>
     * Richiede username, password, nome e cognome, con validazione:
     * <ul>
     *   <li>Username: nessuno spazio e non inizia con '/'</li>
     *   <li>Password: almeno 8 caratteri</li>
     *   <li>Nome e cognome: non vuoti</li>
     * </ul>
     * Dopo la raccolta, invoca {@link SignUpController#registraNuovoUtente}.
     * <p>
     * Gestisce i comandi di navigazione (/b, /q) tramite
     * {@link CommandException}, restituendo l'azione corrispondente.
     *
     * @return navigazione successiva:
     *         <ul>
     *           <li>{@link Navigazione#indietro()} per tornare indietro in caso di successo o comando</li>
     *         </ul>
     */
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

            boolean registrazioneRiuscita = signUpController.registraNuovoUtente(username, password, nome, cognome);
            if (registrazioneRiuscita) {
                ViewUtils.println(ColorUtils.ANSI_GREEN + "\nRegistrazione completata con successo!" + ColorUtils.ANSI_RESET);
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + "\nERRORE: Username non disponibile." + ColorUtils.ANSI_RESET);
            }
            InputUtils.pressEnterToContinue(CostantiView.INVIO_PER_CONTINUARE);
            return Navigazione.indietro();

        } catch (CommandException e) {
            // Comando di navigazione ricevuto: torniamo indietro o logout
            return e.getNavigazione();
        }
    }
}
