package com.chatmulticanale.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for secure password operations including hashing and verification.
 * Utilizza l'algoritmo BCrypt che incorpora automaticamente il salt nell'hash.
 * Offre metodi statici per generare e verificare password hashate in modo sicuro.
 */
public final class PasswordUtils {

    /**
     * Fattore di costo predefinito per BCrypt (work factor).
     * Valori più elevati aumentano la sicurezza ma rallentano la computazione.
     */
    private static final int DEFAULT_COST = 12;

    // Costruttore privato per evitare istanziazione
    private PasswordUtils() {
    }

    /**
     * Esegue l'hash di una password in chiaro utilizzando BCrypt con un salt generato casualmente.
     *
     * @param plainTextPassword la password in chiaro da hashare
     * @return la password hashata, contenente automaticamente il salt
     * @see BCrypt#gensalt(int)
     * @see BCrypt#hashpw(String, String)
     */
    public static String hashPassword(String plainTextPassword) {
        // Genera un salt con il fattore di costo specificato
        String salt = BCrypt.gensalt(DEFAULT_COST);
        // Esegue l'hash con il salt generato
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    /**
     * Verifica se una password in chiaro corrisponde a un hash BCrypt presente.
     *
     * @param plainTextPassword la password in chiaro da verificare
     * @param hashedPassword    l'hash BCrypt di riferimento
     * @return {@code true} se la password corrisponde all'hash, {@code false} altrimenti
     * @see BCrypt#checkpw(String, String)
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // BCrypt estrae il salt dall'hash, ri-hasha la password in chiaro e confronta i risultati
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
