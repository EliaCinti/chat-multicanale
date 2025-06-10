package com.chatmulticanale.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestisce la connessione al database come un Singleton.
 * Carica le credenziali da un file .properties per evitare di scriverle nel codice.
 */
public class DatabaseConnector {
    private static Connection conn = null;
    private static final Properties props = new Properties();

    // Blocco di inizializzazione statico.
    // Viene eseguito UNA SOLA VOLTA, quando la classe viene caricata in memoria.
    static {
        // Usiamo un try-with-resources per assicurarci che l'InputStream venga chiuso.
        try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("Errore: impossibile trovare il file config.properties");
                // Potresti anche lanciare un'eccezione qui per bloccare l'avvio dell'app
            } else {
                props.load(input); // Carica le proprietà dal file
            }
        } catch (IOException ex) {
            System.err.println("Errore durante la lettura del file di configurazione.");
            ex.printStackTrace();
        }
    }

    /**
     * Restituisce un'istanza della connessione al database.
     * Se la connessione non esiste o è chiusa, ne crea una nuova.
     * @return L'oggetto Connection.
     */
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(
                        props.getProperty("db.url"),
                        props.getProperty("db.user"),
                        props.getProperty("db.password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la connessione al database.");
            e.printStackTrace(); // Qui andrebbe una gestione più robusta dell'eccezione
        }
        return conn;
    }

    // Metodo opzionale per chiudere la connessione quando l'applicazione termina.
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connessione al database chiusa.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
