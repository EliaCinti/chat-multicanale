package com.chatmulticanale.model;

/**
 * Enum che definisce i ruoli applicativi disponibili all'interno del sistema.
 * Utilizzato per gestire autorizzazioni e permessi degli utenti.
 */
public enum Ruolo {
    /**
     * Ruolo con privilegi completi di amministratore di sistema.
     */
    amministratore,
    /**
     * Ruolo assegnato ai capi progetto per gestire i progetti e supervisionare le chat.
     */
    capoprogetto,
    /**
     * Ruolo di base per dipendenti senza privilegi amministrativi sulla piattaforma.
     */
    dipendente;

    /**
     * Converte una stringa in un valore dell'enum {@link Ruolo}.
     * Effettua il confronto in modo case-insensitive.
     *
     * @param text stringa rappresentante un ruolo
     * @return valore enum corrispondente a {@code text}
     * @throws IllegalArgumentException se {@code text} non corrisponde a nessun ruolo definito
     */
    public static Ruolo fromString(String text) {
        for (Ruolo r : Ruolo.values()) {
            if (r.name().equalsIgnoreCase(text)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Nessun ruolo trovato con il testo: " + text);
    }
}
