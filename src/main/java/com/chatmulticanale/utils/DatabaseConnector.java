package com.chatmulticanale.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestisce la connessione al database utilizzando il pattern Singleton.
 * <p>
 * Carica le proprietà di connessione da un file {@code config.properties} presente nel classpath.
 * Espone metodi statici per ottenere e chiudere la connessione condivisa.
 */
public final class DatabaseConnector {

    // Logger per i messaggi di errore e informazione
    private static final Logger logger = Logger.getLogger(DatabaseConnector.class.getName());

    // Connessione singleton condivisa
    private static Connection conn = null;

    // Proprietà di configurazione (URL, utente, password)
    private static final Properties props = new Properties();

    // Costruttore privato per evitare l'instanziazione
    private DatabaseConnector() {
    }

    // Inizializzazione statica: carica il file di configurazione
    static {
        try (InputStream input = DatabaseConnector.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.log(Level.SEVERE,
                        "ERRORE FATALE: Impossibile trovare il file config.properties nel classpath.");
            } else {
                props.load(input);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE,
                    "ERRORE FATALE: Impossibile leggere il file di configurazione.", ex);
        }
    }

    /**
     * Restituisce la connessione singleton al database.
     * <p>
     * Se la connessione non è ancora stata aperta o è chiusa, ne crea una nuova
     * utilizzando le proprietà caricate dal file di configurazione.
     *
     * @return l'istanza di {@link Connection} se creata con successo, altrimenti {@code null}
     */
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                if (props.isEmpty()) {
                    logger.severe("Proprietà del database non caricate. Impossibile creare la connessione.");
                    return null;
                }
                conn = DriverManager.getConnection(
                        props.getProperty("db.url"),
                        props.getProperty("db.user"),
                        props.getProperty("db.password")
                );
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Errore durante la connessione al database. Verificare URL, utente e password.",
                    e);
            return null;
        }
        return conn;
    }

    /**
     * Chiude la connessione al database se è aperta.
     * <p>
     * Dovrebbe essere chiamato alla terminazione dell'applicazione per rilasciare le risorse.
     */
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.info("Connessione al database chiusa con successo.");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING,
                    "Errore durante la chiusura della connessione al database.",
                    e);
        }
    }
}