package com.chatmulticanale.model;

/**
 * Enum che rappresenta il contesto di una conversazione nel sistema.
 * Può essere utilizzato per distinguere tra comunicazioni in un canale di progetto
 * o in una chat privata.
 */
public enum TipoContestoChat {

    /**
     * Contesto di conversazione in un canale di progetto pubblico all'interno del sistema.
     */
    CANALE_PROGETTO,

    /**
     * Contesto di conversazione in una chat privata tra due utenti.
     */
    CHAT_PRIVATA
}
