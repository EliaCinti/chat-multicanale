package com.chatmulticanale.model;

import javax.management.relation.Role;

public enum Ruolo {
    amministratore,
    capoprogetto,
    dipendente;

    /**
     * Un metodo di utilità per convertire una stringa (dal DB) in un Enum.
     * Gestisce anche il caso in cui la stringa non corrisponda a nessun valore.
     *
     * @param text La stringa del ruolo.
     * @return L'enum Ruolo corrispondente.
     * @throws IllegalArgumentException se il testo non è un ruolo valido.
     */
    public static Ruolo fromString(String text) {
        for (Ruolo r : Ruolo.values()) {
            // Confronto case-insensitive per sicurezza
            if (r.name().equalsIgnoreCase(text)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Nessun ruolo trovato con il testo: " + text);
    }
}
