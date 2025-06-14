package com.chatmulticanale.view.navigation;

import java.util.Stack;

/**
 * Gestisce il flusso di navigazione tra le diverse {@link View} dell'applicazione.
 * Utilizza uno Stack per mantenere una cronologia delle viste, permettendo
 * una navigazione fluida avanti e indietro.
 */
public class NavigationManager {

    private final Stack<View> history = new Stack<>();

    /**
     * Avvia il ciclo di navigazione.
     * @param vistaIniziale La prima vista da mostrare all'utente dopo il login.
     */
    public void start(View vistaIniziale) {
        // Aggiunge la prima schermata (es. La home del ruolo) allo stack.
        history.push(vistaIniziale);

        // Il ciclo continua finché ci sono viste nello stack.
        while (!history.isEmpty()) {
            // Guarda qual è la vista corrente (quella in cima allo stack).
            View vistaCorrente = history.peek();

            // Esegue la vista e attende che restituisca un'istruzione di navigazione.
            Navigazione risultato = vistaCorrente.show();

            // Esegue l'azione richiesta dalla vista.
            switch (risultato.azione) {
                case VAI_A:
                    // Aggiunge la nuova vista in cima allo stack.
                    history.push(risultato.prossimaVista);
                    break;
                case INDIETRO:
                    // Rimuove la vista corrente, se non è l'unica rimasta.
                    if (history.size() > 1) {
                        history.pop();
                    }
                    break;
                case LOGOUT:
                    // Rimuovi tutte le viste dallo stack FINO A CHE non rimane solo la prima (la WelcomeView).
                    while (history.size() > 1) {
                        history.pop();
                    }
                    break;
                case EXIT:
                    // Svuota completamente lo stack. Questo farà terminare il loop while.
                    history.clear();
                    break;
            }
        }
    }
}