package com.chatmulticanale.dao;

import com.chatmulticanale.dao.costanti.CostantiCanaleProgettoDAO;
import com.chatmulticanale.dao.costanti.CostantiProgettoDAO;
import com.chatmulticanale.model.CanaleProgetto;
import com.chatmulticanale.utils.DatabaseConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO per la gestione dei canali di progetto nel database.
 * Fornisce metodi per creare nuovi canali, recuperare canali di un progetto
 * e ottenere i canali a cui partecipa un utente.
 * <p>
 * Utilizza Stored Procedures definite in {@link CostantiCanaleProgettoDAO}
 * e query SQL definite in {@link CostantiProgettoDAO}.
 * Gestisce la connessione tramite {@link DatabaseConnector}.
 */
public class CanaleProgettoDAO {

    private static final Logger logger = Logger.getLogger(CanaleProgettoDAO.class.getName());

    /**
     * Crea un nuovo canale di progetto e aggiunge automaticamente il creatore come partecipante.
     * La Stored Procedure {@code sp_CP1_CreaCanaleProgetto} esegue entrambe le operazioni
     * in un'unica transazione.
     *
     * @param nuovoCanale       l'oggetto {@link CanaleProgetto} contenente nome e descrizione del canale
     * @param idProgetto        identificativo del progetto a cui associare il canale
     * @param idUtenteCreatore  identificativo del capo progetto creatore del canale
     * @throws SQLException se la connessione è assente o si verifica un errore SQL durante l'esecuzione
     * @see CostantiCanaleProgettoDAO#SP_CREA_CANALE
     * @see DatabaseConnector#getConnection()
     */
    public void creaNuovoCanale(CanaleProgetto nuovoCanale, int idProgetto, int idUtenteCreatore) throws SQLException {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire creaNuovoCanale perchè la connessione al database è assente.");
            return;
        }

        try (CallableStatement stmt = conn.prepareCall(CostantiCanaleProgettoDAO.SP_CREA_CANALE)) {
            // I parametri sono: p_Nome_Canale, p_Descrizione_Canale, p_ID_Progetto, p_ID_Utente_Creatore
            stmt.setString(1, nuovoCanale.getNomeCanale());
            stmt.setString(2, nuovoCanale.getDescrizioneCanale());
            stmt.setInt(3, idProgetto);
            stmt.setInt(4, idUtenteCreatore);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Impossibile creare il canale '" + nuovoCanale.getNomeCanale() + "' per il progetto ID: " + idProgetto, e);
        }
    }

    /**
     * Recupera tutti i canali associati a un progetto.
     *
     * @param idProgetto identificativo del progetto di cui ottenere i canali
     * @return lista di {@link CanaleProgetto}; lista vuota se non ci sono canali o in caso di errore di connessione
     * @see CostantiCanaleProgettoDAO#SELECT_CANALI_DI_PROGETTO
     * @see DatabaseConnector#getConnection()
     */
    public List<CanaleProgetto> getCanaliPerProgetto(int idProgetto) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire getCanaliPerProgetto perchè la connessione al database è assente.");
            return Collections.emptyList();
        }

        List<CanaleProgetto> canali = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(CostantiCanaleProgettoDAO.SELECT_CANALI_DI_PROGETTO)) {
            stmt.setInt(1, idProgetto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CanaleProgetto canale = new CanaleProgetto();
                    canale.setIdCanale(rs.getInt(CostantiCanaleProgettoDAO.ID_CANALE));
                    canale.setNomeCanale(rs.getString(CostantiCanaleProgettoDAO.NOME_CANALE));
                    canali.add(canale);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei canali per il progetto ID: " + idProgetto);        }
        return canali;
    }

    /**
     * Ottiene i canali di progetto a cui un utente partecipa.
     * Utilizza la Stored Procedure {@code sp_UT6} per il recupero.
     *
     * @param idUtente identificativo dell'utente
     * @return lista di {@link CanaleProgetto}; lista vuota in caso di errore di connessione o SQL
     * @see CostantiCanaleProgettoDAO#SP_GET_CANALI_PARTECIPATI
     * @see DatabaseConnector#getConnection()
     */
    public List<CanaleProgetto> getCanaliPartecipatiDaUtente(int idUtente) {
        Connection conn = DatabaseConnector.getConnection();
        if (conn == null) {
            logger.log(Level.SEVERE, "Impossibile eseguire getCanaliPartecipatiDaUtente perchè la connessione al database è assente.");
            return Collections.emptyList();
        }

        List<CanaleProgetto> canali = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(CostantiCanaleProgettoDAO.SP_GET_CANALI_PARTECIPATI)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CanaleProgetto canale = new CanaleProgetto();
                    canale.setIdCanale(rs.getInt(CostantiCanaleProgettoDAO.ID_CANALE));
                    canale.setNomeCanale(rs.getString(CostantiCanaleProgettoDAO.NOME_CANALE));
                    canale.setDescrizioneCanale(rs.getString(CostantiCanaleProgettoDAO.DESCRIZIONE_CANALE));
                    canale.setIdProgetto(rs.getInt(CostantiProgettoDAO.ID_PROGETTO));
                    canali.add(canale);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e, () -> "Errore durante il recupero dei canali per l'utente ID: " + idUtente);
        }
        return canali;
    }
}
