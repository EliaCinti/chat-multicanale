package com.chatmulticanale.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger; // <-- 1. IMPORTA IL LOGGER

/**
 * Gestisce la connessione al database come un Singleton.
 * Carica le credenziali da un file.properties per evitare di scriverle nel codice.
 */
public class DatabaseConnector {

    private DatabaseConnector() {
        // costruttore privato
    }

    private static final Logger logger = Logger.getLogger(DatabaseConnector.class.getName());

    private static Connection conn = null;
    private static final Properties props = new Properties();

    // Blocco di inizializzazione statico.
    static {
        try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                // Errore critico: l'app non può partire senza configurazione.
                logger.log(Level.SEVERE, "ERRORE FATALE: Impossibile trovare il file config.properties nel classpath.");
            } else {
                props.load(input);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "ERRORE FATALE: Impossibile leggere il file di configurazione.", ex);
        }
    }

    /**
     * Restituisce un'istanza della connessione al database.
     * Se la connessione non esiste o è chiusa, ne crea una nuova.
     * @return L'oggetto Connection, o null se la connessione fallisce.
     */
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                if (props.isEmpty()) {
                    logger.severe("Le proprietà del database non sono state caricate, impossibile creare la connessione.");
                    return null;
                }
                conn = DriverManager.getConnection(
                        props.getProperty("db.url"),
                        props.getProperty("db.user"),
                        props.getProperty("db.password")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante la connessione al database. Controllare URL, utente e password nel file di configurazione.", e);
            return null;
        }
        return conn;
    }

    /**
     * Chiude la connessione al database se è aperta.
     * Questo metodo dovrebbe essere chiamato alla chiusura dell'applicazione.
     */
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.info("Connessione al database chiusa con successo.");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Errore durante la chiusura della connessione al database.", e);
        }
    }
}