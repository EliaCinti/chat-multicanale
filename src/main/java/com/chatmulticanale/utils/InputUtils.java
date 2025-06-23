package com.chatmulticanale.utils;

import com.chatmulticanale.exception.CommandException;
import com.chatmulticanale.view.navigation.Navigazione;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Utility class per gestire l'input dell'utente in console in modo centralizzato e robusto.
 * <p>
 * Utilizza una singola istanza di {@link Scanner} per l'intera applicazione.
 * Gestisce comandi di navigazione (es. "/b", "/q") lanciando {@link CommandException}
 * e fornisce metodi per la validazione e la lettura di stringhe e numeri interi.
 */
public final class InputUtils {

    // Singola istanza di Scanner per evitare conflitti di input
    private static final Scanner scanner = new Scanner(System.in);

    // Costruttore privato per evitare istanziazione
    private InputUtils() {
    }

    /**
     * Chiede una stringa all'utente con validazione personalizzata.
     * <p>
     * Verifica prima se l'input corrisponde a un comando di navigazione ("/b" o "/q").
     * In caso affermativo, lancia {@link CommandException} con la navigazione corrispondente.
     * Altrimenti, valida il dato con il {@code validator} e, in caso di fallimento,
     * mostra {@code errorMessage} e ripete la richiesta.
     *
     * @param prompt       il messaggio da mostrare all'utente
     * @param validator    predicato per validare l'input utente
     * @param errorMessage messaggio di errore da mostrare in caso di validazione fallita
     * @return la stringa validata dall'utente
     * @throws CommandException se viene inserito un comando di navigazione ("/b", "/q")
     */
    public static String askForInput(String prompt, Predicate<String> validator, String errorMessage) throws CommandException {
        while (true) {
            ViewUtils.print(prompt);
            String input = scanner.nextLine();

            // Controllo comandi di navigazione
            if (input.equalsIgnoreCase("/b") || input.equalsIgnoreCase("/back")) {
                throw new CommandException(Navigazione.indietro());
            }
            if (input.equalsIgnoreCase("/q") || input.equalsIgnoreCase("/quit")) {
                throw new CommandException(Navigazione.logout());
            }

            // Validazione dei dati
            if (validator.test(input)) {
                return input;
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + errorMessage + ColorUtils.ANSI_RESET);
            }
        }
    }

    /**
     * Overload di {@link #askForInput(String, Predicate, String)} che richiede solo che
     * la stringa non sia vuota.
     *
     * @param prompt il messaggio da mostrare all'utente
     * @return la stringa non vuota inserita dall'utente
     * @throws CommandException se viene inserito un comando di navigazione
     */
    public static String askForInput(String prompt) throws CommandException {
        return askForInput(prompt, s -> !s.trim().isEmpty(), "Errore: Il campo non può essere vuoto.");
    }

    /**
     * Legge e restituisce un intero dall'utente.
     * <p>
     * Usa {@link #askForInput(String)} per gestire comandi di navigazione
     * e poi converte la stringa in intero, ritentando in caso di formato errato.
     *
     * @param prompt il messaggio da mostrare all'utente
     * @return l'intero inserito dall'utente
     * @throws CommandException se viene inserito un comando di navigazione
     */
    public static int readInt(String prompt) throws CommandException {
        while (true) {
            String input = askForInput(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Inserisci un numero valido." + ColorUtils.ANSI_RESET);
            }
        }
    }

    /**
     * Legge e restituisce un intero all'interno di un range inclusivo.
     * <p>
     * Richiama {@link #readInt(String)} e verifica che il valore sia tra {@code min} e {@code max}.
     * In caso contrario, mostra messaggio di errore e ripete.
     *
     * @param prompt il messaggio da mostrare all'utente
     * @param min    valore minimo accettabile
     * @param max    valore massimo accettabile
     * @return l'intero valido inserito dall'utente
     * @throws CommandException se viene inserito un comando di navigazione
     */
    public static int readIntInRange(String prompt, int min, int max) throws CommandException {
        while (true) {
            int numero = readInt(prompt);
            if (numero >= min && numero <= max) {
                return numero;
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED +
                        "Errore: La scelta deve essere compresa tra " + min + " e " + max + "." +
                        ColorUtils.ANSI_RESET);
            }
        }
    }

    /**
     * Mostra un prompt e attende che l'utente prema Invio per continuare.
     * Utile per mettere in pausa l'applicazione tra schermate.
     *
     * @param prompt il messaggio di pausa (es. "Premi Invio per continuare...")
     */
    public static void pressEnterToContinue(String prompt) {
        ViewUtils.print(prompt);
        scanner.nextLine();
    }

    /**
     * Chiude lo scanner condiviso. Deve essere chiamato alla fine dell'applicazione
     * per rilasciare le risorse di input.
     */
    public static void closeScanner() {
        scanner.close();
    }
}