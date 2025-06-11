package com.chatmulticanale.utils;

import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Classe di utilità per gestire l'input dell'utente in modo centralizzato e robusto.
 * Utilizza una singola istanza di Scanner per evitare conflitti.
 */
public class InputUtils {

    // Un'unica istanza di Scanner per tutta l'applicazione.
    private static final Scanner scanner = new Scanner(System.in);

// Sostituisci il vecchio readString con questo
    /**
     * Legge una riga di testo dall'input dell'utente, assicurandosi che non sia vuota.
     * Continua a chiedere l'input finché non viene fornito un valore valido.
     * @param prompt Il messaggio da mostrare all'utente (es. "Username: ").
     * @return La stringa NON VUOTA inserita dall'utente.
     */
    public static String readString(String prompt) {
        String input;
        while (true) {
            ViewUtils.print(prompt);
            input = scanner.nextLine();

            // Usiamo trim() per rimuovere spazi bianchi all'inizio e alla fine,
            // poi controlliamo se la stringa risultante è vuota.
            if (input != null && !input.trim().isEmpty()) {
                return input; // Input valido, esci dal loop e restituiscilo.
            } else {
                // Input non valido, mostra un errore e il loop ricomincia.
                ViewUtils.println(ColorUtils.ANSI_RED + "Errore: Questo campo non può essere vuoto. Riprova." + ColorUtils.ANSI_RESET);
            }
        }
    }

    /**
     * Legge una stringa dall'utente e la valida usando una regola custom.
     * Continua a chiedere finché l'input non soddisfa la regola.
     * @param prompt Il messaggio da mostrare all'utente.
     * @param validator Una regola (Predicate) che restituisce true se l'input è valido.
     * @param errorMessage Il messaggio di errore da mostrare se la validazione fallisce.
     * @return La stringa validata.
     */
    public static String readValidatedString(String prompt, Predicate<String> validator, String errorMessage) {
        while (true) {
            String input = readString(prompt); // Usa il nostro metodo base per assicurarsi che non sia vuoto
            if (validator.test(input)) {
                return input; // La regola è soddisfatta, restituisci l'input.
            } else {
                ViewUtils.println(ColorUtils.ANSI_RED + errorMessage + ColorUtils.ANSI_RESET);
            }
        }
    }

    /**
     * Legge un numero intero dall'input dell'utente.
     * Continua a chiederlo finché non viene inserito un intero valido.
     * @param prompt Il messaggio da mostrare all'utente.
     * @return L'intero inserito.
     */
    public static int readInt(String prompt) {
        while (true) {
            String input = readString(prompt); // Riusiamo il nostro metodo per leggere stringhe
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ViewUtils.println("Errore: Inserisci un numero intero valido.");
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