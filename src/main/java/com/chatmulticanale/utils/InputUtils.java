package com.chatmulticanale.utils;

import java.util.Scanner;

/**
 * Classe di utilità per gestire l'input dell'utente in modo centralizzato e robusto.
 * Utilizza una singola istanza di Scanner per evitare conflitti.
 */
public class InputUtils {

    // Un'unica istanza di Scanner per tutta l'applicazione.
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Legge una riga di testo dall'input dell'utente.
     * @param prompt Il messaggio da mostrare all'utente (es. "Username: ").
     * @return La stringa inserita dall'utente.
     */
    public static String readString(String prompt) {
        ViewUtils.print(prompt); // Usiamo la nostra utility di vista!
        return scanner.nextLine();
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

    // Metodo per chiudere lo scanner quando l'applicazione termina.
    public static void closeScanner() {
        scanner.close();
    }
}