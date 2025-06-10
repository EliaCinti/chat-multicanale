package com.chatmulticanale.view.navigation;

/**
 * Un oggetto che rappresenta un'istruzione di navigazione.
 * Viene restituito da una {@link View} per dire al {@link NavigationManager}
 * quale azione compiere (andare a una nuova vista, tornare indietro, o fare logout).
 */
public class Navigazione {

    /**
     * Enum che definisce le possibili azioni di navigazione.
     */
    public enum Azione {
        VAI_A,      // Naviga verso una nuova vista specificata.
        INDIETRO,   // Torna alla vista precedente nello stack.
        LOGOUT      // Termina la sessione utente e svuota lo stack di navigazione.
    }

    public final Azione azione;
    public final View prossimaVista; // Usato solo se l'azione è VAI_A.

    /**
     * Costruttore privato per forzare l'uso dei metodi factory statici.
     * @param azione L'azione di navigazione da compiere.
     * @param prossimaVista La vista di destinazione (può essere null).
     */
    private Navigazione(Azione azione, View prossimaVista) {
        this.azione = azione;
        this.prossimaVista = prossimaVista;
    }

    /**
     * Factory method per creare un'istruzione di navigazione verso una nuova vista.
     * @param vista La nuova istanza della View da mostrare.
     * @return Un oggetto Navigazione configurato per l'azione VAI_A.
     */
    public static Navigazione vaiA(View vista) {
        return new Navigazione(Azione.VAI_A, vista);
    }

    /**
     * Factory method per creare un'istruzione per tornare alla vista precedente.
     * @return Un oggetto Navigazione configurato per l'azione INDIETRO.
     */
    public static Navigazione indietro() {
        return new Navigazione(Azione.INDIETRO, null);
    }

    /**
     * Factory method per creare un'istruzione di logout.
     * @return Un oggetto Navigazione configurato per l'azione LOGOUT.
     */
    public static Navigazione logout() {
        return new Navigazione(Azione.LOGOUT, null);
    }
}