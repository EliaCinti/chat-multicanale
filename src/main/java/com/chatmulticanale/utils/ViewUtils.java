package com.chatmulticanale.utils;

/**
 * Classe di utilità per standardizzare le operazioni di visualizzazione in console.
 * Fornisce metodi per stampare testo, separatori e pulire lo schermo.
 * Utilizzata da tutte le viste per mantenere coerenza nell'output.
 */
public final class ViewUtils {

    // Costruttore privato per evitare istanziazione
    private ViewUtils() {}

    /**
     * Stampa un messaggio senza andare a capo.
     *
     * @param message stringa da stampare in console
     */
    public static void print(String message) {
        System.out.print(message);
    }

    /**
     * Stampa un messaggio seguito da un carattere di nuova linea.
     *
     * @param message stringa da stampare in console
     */
    public static void println(String message) {
        System.out.println(message);
    }

    /**
     * Stampa una linea di separazione predefinita per delimitare sezioni.
     */
    public static void printSeparator() {
        println("-----------------------------------------------------");
    }

    /**
     * "Pulisce" lo schermo simulando lo scroll di più linee vuote.
     * Utile per ridurre il disordine visivo tra schermate successive.
     */
    public static void clearScreen() {
        for (int i = 0; i < 50; ++i) {
            println("");
        }
    }
}