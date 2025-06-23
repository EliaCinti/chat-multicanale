package com.chatmulticanale.utils;

/**
 * Utility class contenente costanti ANSI per la colorazione dell'output in console.
 * <p>
 * Fornisce codici ANSI per modificare il colore e lo stile del testo durante la stampa su terminale.
 * Può essere utilizzata per enfatizzare messaggi di log, errori o informazioni.
 */
public final class ColorUtils {

    // Costruttore privato per evitare istanziazione
    private ColorUtils() {
    }

    /** Reset di tutti gli attributi di colore e stile. */
    public static final String ANSI_RESET = "\u001B[0m";

    /** Colore verde (32) per evidenziare messaggi positivi o di successo. */
    public static final String ANSI_GREEN = "\u001B[32m";

    /** Colore blu (34) per messaggi informativi. */
    public static final String ANSI_BLUE = "\u001B[34m";

    /** Colore ciano (36) per messaggi di contesto o secondari. */
    public static final String ANSI_CYAN  = "\u001B[36m";

    /** Colore rosso (31) per segnalare errori o avvisi critici. */
    public static final String ANSI_RED = "\u001B[31m";

    /** Colore giallo (33) per avvisi o messaggi di attenzione. */
    public static final String ANSI_YELLOW = "\u001B[33m";

    /** Stile grassetto (1) per enfatizzare il testo. */
    public static final String ANSI_BOLD = "\u001B[1m";
}
