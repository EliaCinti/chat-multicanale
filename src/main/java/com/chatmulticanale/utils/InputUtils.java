package com.chatmulticanale.utils;

import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.view.navigation.Navigazione;

import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Classe di utilità per gestire l'input dell'utente in modo centralizzato e robusto.
 * Utilizza una singola istanza di Scanner per evitare conflitti.
 */
public class InputUtils {

    // Un'unica istanza di Scanner per tutta l'applicazione.
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Metodo principale e definitivo per chiedere un input all'utente.
     * Legge una riga e PRIMA controlla se è un comando. Se lo è, lancia una CommandException.
     * Se non è un comando, lo valida come dato.
     *
     * @param prompt Il messaggio da mostrare.
     * @param validator La regola di validazione per i dati.
     * @param errorMessage Il messaggio di errore per la validazione.
     * @return La stringa validata.
     * @throws CommandException se l'utente inserisce un comando come "/b" o "/q".
     */
    public static String askForInput(String prompt, Predicate<String> validator, String errorMessage) throws CommandException {
        while (true) {
            ViewUtils.print(prompt);
            String input = scanner.nextLine();

            // 1. CONTROLLO COMANDI: Questo scatta prima di qualsiasi altra cosa.
            if (input.equalsIgnoreCase("/b") || input.equalsIgnoreCase("/back")) {
                throw new CommandException(Navigazione.indietro());
            }
            if (input.equalsIgnoreCase("/q") || input.equalsIgnoreCase("/quit")) {
                throw new CommandException(Navigazione.logout());
            }

            // 2. VALIDAZIONE DATI: Eseguita solo se l'input non era un comando.
            if (validator.test(input)) {
                return input; // Dato valido
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + errorMessage + ColorUtils.ANSI_RESET);
            }
        }
    }

    // Un overload per quando basta che l'input non sia vuoto
    public static String askForInput(String prompt) throws CommandException {
        return askForInput(prompt, s -> !s.trim().isEmpty(), "Errore: Il campo non può essere vuoto.");
    }

    /**
     * Metodo robusto per leggere un numero intero dall'utente.
     * Gestisce input non numerici e i comandi di navigazione.
     *
     * @param prompt Il messaggio da mostrare.
     * @return L'intero inserito dall'utente.
     * @throws CommandException se l'utente inserisce un comando.
     */
    public static int readInt(String prompt) throws CommandException {
        while (true) {
            // Usiamo il nostro metodo base per leggere l'input.
            // La validazione qui è semplice perché la vera validazione è la conversione a intero.
            String input = askForInput(prompt, s -> true, ""); // Accetta qualsiasi stringa (che non sia un comando)

            try {
                // Tenta di convertire la stringa in un numero.
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // Se la conversione fallisce, mostra un errore e il loop ricomincia.
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Inserisci un numero valido." + ColorUtils.ANSI_RESET);
            }
        }
    }

    /**
     * Mostra un messaggio e attende semplicemente che l'utente prema il tasto Invio.
     * Questo metodo è utile per "mettere in pausa" l'applicazione, non per raccogliere dati,
     * e quindi NON esegue alcuna validazione sull'input.
     *
     * @param prompt Il messaggio da mostrare all'utente (es. "Premi Invio per continuare...").
     */
    public static void pressEnterToContinue(String prompt) {
        ViewUtils.print(prompt); // Usiamo ViewUtils per coerenza
        scanner.nextLine();      // Legge semplicemente la riga, qualsiasi essa sia, e la ignora.
    }

    // Metodo per chiudere lo scanner quando l'applicazione termina.
    public static void closeScanner() {
        scanner.close();
    }
}